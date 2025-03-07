package haidnor.jvm.rtda;

/**
 * @author wang xiang
 */
public class InstanceArray extends ArrayInstance {

    public final Instance[] items;

    public InstanceArray(Klass klass, Instance[] items) {
        super(klass, items.length);
        this.items = items;
    }

}