package haidnor.jvm.instruction.references;

import haidnor.jvm.instruction.Instruction;
import haidnor.jvm.rtda.Instance;
import haidnor.jvm.runtime.Frame;
import haidnor.jvm.runtime.StackValue;
import haidnor.jvm.util.CodeStream;
import haidnor.jvm.util.ConstantPoolUtil;
import lombok.SneakyThrows;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Utility;

/**
 * @author wang xiang
 */
public class INSTANCEOF extends Instruction {

    private final int constantClassIndex;

    public INSTANCEOF(CodeStream codeStream) {
        super(codeStream);
        this.constantClassIndex = codeStream.readUnsignedShort(this);
    }

    @Override
    @SneakyThrows
    public void execute(Frame frame) {
        ConstantPoolUtil constantPoolUtil = frame.getConstantPoolUtil();

        String className = constantPoolUtil.constantClass_ClassName(constantClassIndex);
        className = Utility.compactClassName(className);

        StackValue stackValue = frame.pop();
        Object obj = stackValue.getValue();
        Class<?> objClass = obj.getClass();

        if (obj instanceof Instance instance) {
            boolean result = findClassFromSuper(instance.klass.getJavaClass(), className);
            if (!result)  {
                result = findClassFromInterface(instance.klass.getJavaClass(), className);
            }
            if (result) {
                frame.push(new StackValue(Const.T_INT, 1));
            } else {
                frame.push(new StackValue(Const.T_INT, 0));
            }
        } else {
            if (Class.forName(className).isAssignableFrom(objClass)) {
                frame.push(new StackValue(Const.T_INT, 1));
            } else {
                frame.push(new StackValue(Const.T_INT, 0));
            }
        }
    }

    public boolean findClassFromSuper(JavaClass javaClass, String className) throws ClassNotFoundException {
        if (javaClass.getClassName().equals(className)) {
            return true;
        }
        for (JavaClass superClass : javaClass.getSuperClasses()) {
            if (superClass.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public boolean findClassFromInterface(JavaClass javaClass, String className) throws ClassNotFoundException {
        for (JavaClass anInterface : javaClass.getInterfaces()) {
            if (anInterface.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

}
