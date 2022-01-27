package com.reflectoring.annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.apache.commons.text.CaseUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.reflectoring.annotation.processor.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {

        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element typeElement : roundEnv.getElementsAnnotatedWith(Builder.class)) {

            List<Element> fieldElements = typeElement.getEnclosedElements().stream().filter(e -> ElementKind.FIELD.equals(e.getKind())).collect(
                    Collectors.toList());

            String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            String className = typeElement.getSimpleName().toString();
            String builderName = String.format("%sBuilder", typeElement.getSimpleName().toString());
            String classVariableName = CaseUtils.toCamelCase(typeElement.getSimpleName().toString(), false, '_');

            try {
                //writeBuilderClass(packageName, className, classVariableName, builderName, fieldElements);

                writeJavaPoetBuilderClass(packageName, className, classVariableName, builderName, fieldElements, typeElement);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Failed to write file for element", typeElement);
            }
        }

        return true;
    }

    private String getBaseName(String name) {

        int lastPeriodIndex = name.lastIndexOf('.');
        if (lastPeriodIndex > 0) {
            name = name.substring(lastPeriodIndex + 1);
        }

        return name;
    }

    private void writeBuilderClass(String packageName, String className, String classVariableName, String builderName,
                                   List<Element> fieldElements) throws IOException {

        JavaFileObject builder = processingEnv.getFiler().createSourceFile(builderName);

        try (PrintWriter out = new PrintWriter(builder.openWriter())) {

            // Write the Package name
            out.print("package ");
            out.print(packageName);
            out.println(";");
            out.println();

            // Write the Class name
            out.print("public final class ");
            out.print(builderName);
            out.println(" {");
            out.println();

            // Write the Field names
            for (Element fieldElement : fieldElements) {

                TypeMirror typeMirror = fieldElement.asType();

                String fieldTypeName = getBaseName(typeMirror.toString());
                String fieldName = getBaseName(fieldElement.getSimpleName().toString());

                out.print("private ");
                out.print(fieldTypeName);
                out.print(" ");
                out.print(fieldName);
                out.print(";");
                out.println();
            }

            out.println();

            // Write the Setters
            for (Element fieldElement : fieldElements) {

                TypeMirror typeMirror = fieldElement.asType();

                String fieldTypeName = getBaseName(typeMirror.toString());
                String fieldName = getBaseName(fieldElement.getSimpleName().toString());

                out.print("public ");
                out.print(" ");
                out.print(builderName);
                out.print(" ");
                out.print(fieldName);
                out.print("(");
                out.print(fieldTypeName);
                out.print(" ");
                out.print(fieldName);
                out.print(") {");
                out.println();
                out.print("     this.");
                out.print(fieldName);
                out.print(" = ");
                out.print(fieldName);
                out.print(";");
                out.println();
                out.print("     return this;");
                out.println();
                out.print("}");
                out.println();
                out.println();
            }

            // Write the build function
            out.print("public ");
            out.print(" ");
            out.print(className);
            out.print(" build() {");
            out.println();
            out.print("     ");
            out.print(className);
            out.print(" ");
            out.print(classVariableName);
            out.print(" = new ");
            out.print(className);
            out.print("();");
            out.println();

            for (Element fieldElement : fieldElements) {

                TypeMirror typeMirror = fieldElement.asType();

                String fieldTypeName = getBaseName(typeMirror.toString());
                String fieldName = getBaseName(fieldElement.getSimpleName().toString());

                out.print("     ");
                out.print(classVariableName);
                out.print(".set");
                out.print(CaseUtils.toCamelCase(fieldName, true, '_'));
                out.print("(this.");
                out.print(fieldName);
                out.println(");");
            }

            out.println();
            out.print("     return ");
            out.print(classVariableName);
            out.print(";");
            out.println();
            out.println("   }");
            out.println("}");
        }
    }

    private void writeJavaPoetBuilderClass(String packageName, String className, String classVariableName, String builderName,
                                   List<Element> fieldElements, Element typeElement) throws IOException {

        ClassName builderType = ClassName.get(packageName, builderName);

        List<FieldSpec> fields = new ArrayList<>(fieldElements.size());
        List<MethodSpec> fieldSetters = new ArrayList<>(fieldElements.size());

        // Generate the fields and field setters
        generateFieldsAndSetters(fields, fieldSetters, fieldElements, builderType);

        TypeName targetType = TypeName.get(typeElement.asType());

        // Generate the build method
        MethodSpec buildMethod = generateBuildMethod(targetType, classVariableName, fields);

        TypeSpec builder = TypeSpec.classBuilder(builderType)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fields)
                .addMethods(fieldSetters)
                .addMethod(buildMethod).build();

        JavaFile file = JavaFile.builder(builderType.packageName(), builder.toBuilder().build()).build();

        file.writeTo(filer);
    }

    private void generateFieldsAndSetters(List<FieldSpec> fields, List<MethodSpec> fieldSetters, List<Element> fieldElements, ClassName builderType){

        for (Element fieldElement : fieldElements) {

            TypeName typeName = TypeName.get(fieldElement.asType());
            String fieldName = getBaseName(fieldElement.getSimpleName().toString());

            fields.add(FieldSpec.builder(typeName, fieldName, Modifier.PRIVATE).build());

            fieldSetters.add(
                    MethodSpec.methodBuilder(fieldName)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(builderType)
                            .addParameter(typeName, fieldName)
                            .addStatement("this.$N = $N", fieldName, fieldName)
                            .addStatement("return this").build());
        }
    }

    private MethodSpec generateBuildMethod(TypeName targetType, String variableName, List<FieldSpec> fields) {

        MethodSpec.Builder buildMethodBuilder = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(targetType)
                .addStatement("$1T $2N = new $1T()", targetType, variableName);

        for (FieldSpec field : fields) {

            buildMethodBuilder.addStatement("$1N.set$2N(this.$3N)", variableName, CaseUtils.toCamelCase(field.name, true, '_'), field.name);
        }

        buildMethodBuilder.addStatement("return $N", variableName);

        return buildMethodBuilder.build();
    }
}