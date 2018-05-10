package demo.common;


import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class Constant {
	public static final Prop config = PropKit.use("config.properties");
    public static final String db_dataSource = "db.dataSource.main";
    
	public static final int STATUS_NOAUTH = -11; 
    public static final int STATUS_SUCC = 1;
    public static final int STATUS_FAIL = 0;
    
}
