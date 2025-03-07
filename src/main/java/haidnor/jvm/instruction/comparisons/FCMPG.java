package haidnor.jvm.instruction.comparisons;

import haidnor.jvm.instruction.Instruction;
import haidnor.jvm.runtime.Frame;
import haidnor.jvm.util.CodeStream;
/**
 * @author wang xiang
 */
public class FCMPG extends Instruction {

    public FCMPG(CodeStream codeStream) {
        super(codeStream);
    }

    @Override
    public void execute(Frame frame) {
        float v2 = frame.popFloat();
        float v1 = frame.popFloat();
        if (v1 == v2) {
            frame.pushInt(0);
            return;
        }
        if (v1 < v2) {
            frame.pushInt(-1);
            return;
        }
        frame.pushInt(1);
    }

}
