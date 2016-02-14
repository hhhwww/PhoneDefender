package com.xd.phonedefender;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.xd.phonedefender.hw.db.dao.BlackNumberDao;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void add() {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        Random random = new Random();

//        for (int i = 0; i < 200; i++) {
//            boolean add = blackNumberDao.add("1552909622" + i % 10, String.valueOf(random.nextInt(3) + 1));
//            assertEquals(true,add);
//        }

        boolean add = blackNumberDao.add("15529096220", "1");
    }

}