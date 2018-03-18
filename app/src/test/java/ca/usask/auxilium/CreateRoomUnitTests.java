package ca.usask.auxilium;


import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;


import ca.usask.auxilium.auth.CreateRoomActivity;

import static org.junit.Assert.assertEquals;

/**
 * Created by marvib96 on 2018-03-18.
 */

public class CreateRoomUnitTests {

    CreateRoomActivity mRoom ;

    public static String TAG = "CreateRoomActivityTest";

    @Before
    public void setUp(){
        mRoom = new CreateRoomActivity();
    }

    @Test
    public void checkValidEmailTest(){

        try{

            Method emailMethod = mRoom.getClass().getDeclaredMethod("isEmailValid", String.class);
            emailMethod.setAccessible(true);

            //Test 1: string, invalid email
            String email1 = "hi";
            boolean result = (boolean) emailMethod.invoke(mRoom, email1);
            assertEquals(false, result );
            System.out.println("checkValidEmailTest: Test 1 passed");

            //Test 2: invalid email
            String email2 = "";
            result = (boolean) emailMethod.invoke(mRoom, email2);
            assertEquals(false, result);
            System.out.println("checkValidEmailTest: Test 2 passed");

            //Test 3: valid email
            String email3 = "email@gmail.com";
            result = (boolean) emailMethod.invoke(mRoom, email3);
            assertEquals(true, result);
            System.out.println("checkValidEmailTest: Test 3 passed");

            //Test 4: invalid email
            String email4 = "email@.gmail.com";
            result = (boolean) emailMethod.invoke(mRoom, email4);
            assertEquals(true, result);
            System.out.println("checkValidEmailTest: Test 4 passed");

            emailMethod.setAccessible(false);

        }catch (Exception ex){
            System.out.print("Test failed for checkValidEmailTest: " + ex.getCause());
        }
    }

    @Test
    public void checkDefaultEmailTest(){

        try{

            Method emailMethod = CreateRoomActivity.class.getDeclaredMethod("isEmailTheDefaultValue", String.class);
            emailMethod.setAccessible(true);

            //Test 1: Not default email
            boolean result = (boolean) emailMethod.invoke(mRoom, "Not default");
            assertEquals(false, result);
            System.out.println("checkDefaultEmailTest: Test 1 passed");

            //Test 2: valid email
            String defaultEmail = "Email address";
            result = (boolean) emailMethod.invoke(mRoom, defaultEmail);
            assertEquals(true, result);
            System.out.println("checkDefaultEmailTest: Test 2 passed");

            emailMethod.setAccessible(false);

        }catch (Exception ex){
            System.out.println("checkDefaultEmailTest: Test failed " + ex.getMessage());
        }

    }


}
