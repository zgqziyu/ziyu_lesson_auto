package com.ziyu.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.ziyu.admin.modules.system.service.LessonScheduleService;

/**
 * \* User: ziyu
 * \* Date: 2019/8/3
 * \* Time: 16:28
 * \* Description:
 * \
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZiyuTest {

    @Autowired
    private LessonScheduleService lessonScheduleService;

    @Test
    public void Test01(){
        lessonScheduleService.lessonScheduleAuto();
    }

}
