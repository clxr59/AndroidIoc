package com.hankage.butterknife_compiler;

import com.google.auto.service.AutoService;
import com.hankage.butterknife_annotation.BindView;
import com.hankage.butterknife_annotation.OnClick;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Author: cheers
 * Time ： 2020/11/9
 * Description ：
 * @author hankage
 *
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.hankage.butterknife_annotation.BindView", "com.hankage.butterknife_annotation.OnClick"})
public class ButterknifeProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> bindViewSet = roundEnv.getElementsAnnotatedWith(BindView.class);
        Map<String, List<VariableElement>> bindViewMap = getStringListMap(bindViewSet);
        Map<String, List<Element>> onClickMap = getStringMethodListMap(roundEnv.getElementsAnnotatedWith(OnClick.class));

        createSourceFileFromFiler(onClickMap, bindViewMap);


        return false;
    }

    private Map<String, List<Element>> getStringMethodListMap(Set<? extends Element> bindViewSet) {
        Map<String, List<Element>> bindViewMap = new HashMap<>();
        if (!bindViewSet.isEmpty()){
            for (Element element : bindViewSet) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName().toString());
//                Symbol.MethodSymbol variableElement = (Symbol.MethodSymbol) element;
                String activityName = element.getEnclosingElement().getSimpleName().toString();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "activityName = " + activityName);
                List<Element> list = bindViewMap.get(activityName);
                if (list == null){
                    list = new ArrayList<>();
                    bindViewMap.put(activityName, list);
                }
                list.add(element);
            }

        }
        return bindViewMap;
    }

    private Map<String, List<VariableElement>> getStringListMap(Set<? extends Element> bindViewSet) {
        Map<String, List<VariableElement>> bindViewMap = new HashMap<>();
        if (!bindViewSet.isEmpty()){
            for (Element element : bindViewSet) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName().toString());
                VariableElement variableElement = (VariableElement) element;
                String activityName = variableElement.getEnclosingElement().getSimpleName().toString();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "activityName = " + activityName);
                List<VariableElement> list = bindViewMap.get(activityName);
                if (list == null){
                    list = new ArrayList<>();
                    bindViewMap.put(activityName, list);
                }
                list.add(variableElement);
            }

        }
        return bindViewMap;
    }

    private void createSourceFileFromFiler(Map<String, List<Element>> onClickMap, Map<String, List<VariableElement>> map) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "createSourceFileFromFiler");
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String className = iterator.next();
            List<VariableElement> variableElements = map.get(className);
            TypeElement typeElement = (TypeElement) variableElements.get(0).getEnclosingElement();
            // 获得报名
            String packageName=processingEnv.getElementUtils().getPackageOf(typeElement).toString();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "packageName = " + packageName);

           /******************** 开始生成文件 ************************/
            try {
                JavaFileObject file = mFiler.createSourceFile(packageName + "." + className + "_ViewBinding");
                Writer writer = file.openWriter();
                writer.write("package " + packageName + ";\n\n");
                writer.write("import com.hankage.butterknife.Unbinder;\n");
                writer.write("import android.view.View;\n");
                writer.write("import java.util.HashSet;\n");
                writer.write("import java.util.Iterator;\n");


                writer.write("public class " + className + "_ViewBinding implements Unbinder {");
                writer.write("private " + className + " target;\n\n");
                writer.write("private HashSet<View> viewSet = null;\n");
                writer.write("public " + className + "_ViewBinding( final " + className + " target, View view){");
                writer.write("this.target = target;\n");

                int id;
                String varName;
                TypeMirror typeMirror;
                for (VariableElement variableElement : variableElements) {
                    varName = variableElement.getSimpleName().toString();
                    id = variableElement.getAnnotation(BindView.class).value();
                    // 获取类型
                    typeMirror = variableElement.asType();
                    writer.write("target." + varName + " = ("+ typeMirror + " ) target.findViewById(" + id + ");\n");
                }


                // 添加view的 点击事件
                List<Element> clickList = onClickMap.get(className);
                if (clickList != null && !clickList.isEmpty()){
                    writer.write("viewSet = new HashSet();\n");
                    for (Element variableElement : clickList) {
                        OnClick annotation = variableElement.getAnnotation(OnClick.class);
                        int[] value = annotation.value();
                        if (value.length == 0){
                            continue;
                        }
                        varName = variableElement.getSimpleName().toString();
                        for (int viewId : value) {
                            writer.write("View view" + viewId + " = target.findViewById(" + viewId + ");\n");
                            writer.write("view" + viewId + ".setOnClickListener(new View.OnClickListener() {\n\n");
                            writer.write("@Override\n");
                            writer.write("public void onClick(View view){\n");

                            writer.write("target." + varName + "(view);\n");
                            writer.write("}\n");
                            writer.write("});\n");
                            writer.write("viewSet.add(view" + viewId + ");\n");
                        }

                    }

                }

                writer.write("}");


                // 编写unbinder方法
                writer.write("@Override\n");
                writer.write("public void unBinder(){\n");
                writer.write(className + " target = this.target;\n");
                writer.write("if(target == null) return ;\n");
                for (VariableElement variableElement : variableElements) {
                    varName = variableElement.getSimpleName().toString();
                    writer.write("target." + varName + " = null;\n");
                }

                writer.write("this.target = null;\n");

                writer.write("if(viewSet != null && !viewSet.isEmpty()){\n");
                writer.write("Iterator<View> iterator = viewSet.iterator();\n");
                writer.write("while (iterator.hasNext()){\n");
                writer.write("View next = iterator.next();\n");
                writer.write("next.setOnClickListener(null);\n");
                writer.write("next = null;\n");
                writer.write("}\n");
                writer.write("viewSet.clear();\n");
                writer.write("}\n");

                writer.write("}\n");
                writer.write("\n}\n");
                mMessager.printMessage(Diagnostic.Kind.NOTE, "写文件完成");
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
