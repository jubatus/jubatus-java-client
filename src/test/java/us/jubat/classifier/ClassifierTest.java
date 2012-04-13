package us.jubat.classifier;

import org.junit.*;

public class ClassifierTest {
    /*
    public static void main(String[] args){
	try{
	}
    }
    protected void setUp() throws Exception {
	//super.setUp();
    }

    protected void tearDown() throws Exception {
	//super.tearDown();
    }
    */
    @Test
    public void testHello() throws Exception {
	ClassifierClient c = new ClassifierClient("localhost", 9199, 10);
	
	
	//	c.set_config("");
	c.get_config("");
	///	System.out.println("hello");
    }
    @Test
    public void testHello2(){
    }
}
