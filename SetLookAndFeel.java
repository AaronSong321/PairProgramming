package com.aaron;

import javax.swing.UIManager;

public class SetLookAndFeel {
	public static void setNativeLookAndFeel(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){
			System.out.println("…Ë÷√nativeLAF¥ÌŒÛ£∫"+e);
		}
	}
	public static void setJavaLookAndFeel(){
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception e){
			System.out.println("…Ë÷√JavaLAF¥ÌŒÛ£∫"+e);
		}
	}
	public static void setMotifLookAndFeel(){
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch(Exception e){
			System.out.println("…Ë÷√MotifLAF¥ÌŒÛ£∫"+e);
		}
	}
}
