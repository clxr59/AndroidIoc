package com.hankage.hrouter_compiler;

import com.google.auto.service.AutoService;
import com.hankage.hrouter_annotation.HRouter;
import com.hankage.hrouter_annotation.enums.RouteType;
import com.hankage.hrouter_annotation.model.RouteBean;
import com.hankage.hrouter_annotation.model.RouteMeta;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Author: cheers
 * Time ： 2020/11/12
 * Description ：
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.hankage.hrouter_annotation.HRouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(value = {ProcessorConstant.MODULE_NAME})
public class HRouterProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;
    private String mModuleName;
    private Elements mElementUtils;
    private Types mTypeUtils;

    /**
     * 存放每个group里路由信息
     */
    private Map<String, List<RouteBean>> mAllPaths = new HashMap<>();
    /**
     *
     */
    private Map<String, GroupInfo> mAllGroups = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
        mTypeUtils = processingEnv.getTypeUtils();
        mModuleName = processingEnv.getOptions().get(ProcessorConstant.MODULE_NAME);
        if (mModuleName == null){
            mMessager.printMessage(Diagnostic.Kind.ERROR, "请设置module name");
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "mModuleName = " + mModuleName);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        Set<? extends Element> annotations = roundEnv.getElementsAnnotatedWith(HRouter.class);
        if (annotations.isEmpty()){
            mMessager.printMessage(Diagnostic.Kind.NOTE, "没有找到HRouter注解的类");
            return false;
        }

        TypeElement activityElement = mElementUtils.getTypeElement(ProcessorConstant.ACTIVITY);
        TypeMirror activityMirror = activityElement.asType();

        for (Element element : annotations) {
            // 对所有HRouter注解界面进行了获取并转换为RouterMete
            String className = element.getSimpleName().toString();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "className = " + className);

            RouteBean RouteBean = null;

            if (mTypeUtils.isSubtype(element.asType(), activityMirror)){
                // 判断是否为activity类型
                HRouter hRouter = element.getAnnotation(HRouter.class);
                RouteBean = com.hankage.hrouter_annotation.model.RouteBean.build(RouteType.ACTIVITY, element, hRouter.group(), hRouter.path());
            }else {
                RouteBean = com.hankage.hrouter_annotation.model.RouteBean.build(RouteType.UNKNOWN, element, "", "");
            }


            if (checkRouterGroupAndPath(RouteBean)){
                mMessager.printMessage(Diagnostic.Kind.NOTE, "path check success");
                List<RouteBean> list = mAllPaths.get(RouteBean.getGroup());
                if (list == null || list.isEmpty()){
                    list = new ArrayList<>();
                    list.add(RouteBean);
                    mAllPaths.put(RouteBean.getGroup(), list);
                }else {
                    mAllPaths.put(RouteBean.getGroup(), list);
                }
            }

        }

        TypeElement groupElement = mElementUtils.getTypeElement(ProcessorConstant.ROUTER_GROUP);
        TypeElement rootElement = mElementUtils.getTypeElement(ProcessorConstant.ROUTER_ROOT);
        createGroupFile(groupElement);
        createRootFile(rootElement, groupElement);
        return true;
    }

    private void createRootFile(TypeElement rootElement,  TypeElement groupElement) {
        if (mAllGroups.isEmpty()){
            return;
        }

        // Map<String, ? extends IRouteGroup>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(groupElement)));
        ParameterizedTypeName returnParamType = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                parameterizedTypeName);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConstant.ROUTER_ROOT_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(returnParamType);

        // Map<String, ? extend IRouteGroup> roots = new HashMap<>();
        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class), ClassName.get(String.class), parameterizedTypeName,
                "roots", HashMap.class);

        for (Map.Entry<String, GroupInfo> entry : mAllGroups.entrySet()) {
            GroupInfo info = entry.getValue();
            methodBuilder.addStatement("$N.put($S, $T.class)", "roots", entry.getKey(), ClassName.get(info.getPackageName(), info.getClassName()));
        }

        methodBuilder.addStatement("return roots");

        TypeSpec classType = TypeSpec.classBuilder(ProcessorConstant.ROUTER_ROOT_CLASS_NAME + mModuleName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(rootElement))
                .addMethod(methodBuilder.build())
                .build();

        try {
            JavaFile.builder(ProcessorConstant.ROUTER_ROOT_PACKAGE_NAME, classType)
                    .build()
                    .writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建每个model下的注解文件
     * @param groupElement groupElement
     */
    private void createGroupFile(TypeElement groupElement) {
        if (mAllPaths.isEmpty()){
            return;
        }

        // Map<String, RouteBean>;
        ParameterizedTypeName returnParamType = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));

        for (Map.Entry<String, List<RouteBean>> entry : mAllPaths.entrySet()) {
            // 创建方法
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConstant.ROUTER_GROUP_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(returnParamType);

            // Map<String, RouteBean> routes = new HashMap<>();
            methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                    Map.class, String.class, RouteMeta.class, "routes", HashMap.class);

            List<RouteBean> list = entry.getValue();
            for (RouteBean RouteBean : list) {
                // routes.put(path, RouteBean.build(RouteType.Activity, XXX.class, group, path));
                methodBuilder.addStatement("$N.put($S, $T.build($T.$L, $T.class, $S, $S))",
                        "routes",
                        RouteBean.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteType.class), RouteBean.getRouteType(),
                        ClassName.get((TypeElement) RouteBean.getElement()),
                        RouteBean.getGroup(),
                        RouteBean.getPath());
            }

            methodBuilder.addStatement("return routes");

            // 创建类 HRouter$$Group$$xxx
            String className = ProcessorConstant.ROUTER_GROUP_CLASS_NAME + entry.getKey();
            TypeSpec build = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(groupElement))
                    .addMethod(methodBuilder.build())
                    .build();


            // 创建文件 HRouter$$Group$$xxx.java xxx: 表示的是具体的模块名称
            try {
                JavaFile.builder(ProcessorConstant.ROUTER_ROOT_PACKAGE_NAME, build)
                        .build()
                        .writeTo(mFiler);
                GroupInfo info = new GroupInfo(className, ProcessorConstant.ROUTER_ROOT_PACKAGE_NAME);
                mAllGroups.put(entry.getKey(), info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证router信息的准确性
     * @param RouteBean route相关信息
     * @return true 验证成功
     */
    private boolean checkRouterGroupAndPath(RouteBean RouteBean) {
        String group = RouteBean.getGroup();
        String path = RouteBean.getPath();
        if (RouterUtils.isEmpty(path) || !path.startsWith("/") || path.lastIndexOf("/") == 0){
            mMessager.printMessage(Diagnostic.Kind.ERROR, "设置的path格式不对, 标准格式为： /xxx/xxx " + path);
            return false;
        }

        //从path中截取出group
        String pathGroup = path.substring(1, path.indexOf("/", 1));

        if (RouterUtils.isEmpty(group) || !group.equals(mModuleName)){
            if (pathGroup.equals(mModuleName)){
                RouteBean.setGroup(pathGroup);
                return true;
            }else {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "设置的path格式不对, Group 名称应该和model的gradle中保持一致");
                return false;
            }
        }

        return true;
    }
}
