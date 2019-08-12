package com.ziyu.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.ziyu.admin.modules.system.dto.ClassInfo;
import com.ziyu.admin.modules.system.dto.GradeInfoDto;
import com.ziyu.admin.modules.system.dto.SchedulePositionInfo;
import com.ziyu.admin.modules.system.dto.SubjectInfoDto;
import com.ziyu.admin.modules.system.po.User;
import com.ziyu.admin.modules.system.service.impl.ClassScheduling;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WetechAdminApplicationTests {

	@Test
	public void contextLoads() {
		EntityHelper.getOrderByClause(User.class);
	}

	@Test
	public void test(){

		ClassScheduling classScheduling = new ClassScheduling();
		Map<Integer, GradeInfoDto> gradeInfo = new HashMap<Integer, GradeInfoDto>();
		//6个年级 每个年级8个班  主课一个老师2个班  副科一个老师四个班  非文化课一个老师8个班
		//每周5天  每天7节
		for (Integer grade = 0; grade < 2; grade++) {
			GradeInfoDto gi = null;
			//课程信息
			List<SubjectInfoDto> subjectInfos = new ArrayList<SubjectInfoDto>();
			Map<Integer, ClassInfo> classInfos = new HashMap<Integer, ClassInfo>();
			switch (grade) {
				//0年级
				case 0:
					gi = new GradeInfoDto(grade, grade.toString(), 10, 5, 7, 4, 3, 7);

                    /*
                    数学  5、英语 4、语文 5、音乐 1、体育 1、劳动 1、生物  2、地理 2、研学 1、历史 2、
                    道德 2、国学 1、健康 1、专题 1、实践 1、足球 1、美术 1、信息 1、团体活动 1
                     */
					subjectInfos.add(new SubjectInfoDto("shuxue", "数学", 1, 5));
					subjectInfos.add(new SubjectInfoDto("yingyu", "英语", 1, 4));
					subjectInfos.add(new SubjectInfoDto("yuwen", "语文", 1, 5));
					subjectInfos.add(new SubjectInfoDto("yinyue", "音乐", 3, 1));
					subjectInfos.add(new SubjectInfoDto("tiyu", "体育", 3, 1));
					subjectInfos.add(new SubjectInfoDto("laodong", "劳动", 3, 1));
					subjectInfos.add(new SubjectInfoDto("shengwu", "生物", 2, 2));
					subjectInfos.add(new SubjectInfoDto("dili", "地理", 2, 2));
					subjectInfos.add(new SubjectInfoDto("yanxue", "研学", 3, 1));
					subjectInfos.add(new SubjectInfoDto("lishi", "历史", 2, 2));
					subjectInfos.add(new SubjectInfoDto("daode", "道德", 3, 2));
					subjectInfos.add(new SubjectInfoDto("guoxue", "国学", 3, 1));
					subjectInfos.add(new SubjectInfoDto("jiankang", "健康", 3, 1));
					subjectInfos.add(new SubjectInfoDto("zhuanti", "专题", 3, 1));
					subjectInfos.add(new SubjectInfoDto("shijian", "实践", 3, 1));
					subjectInfos.add(new SubjectInfoDto("zuqiu", "足球", 3, 1));
					subjectInfos.add(new SubjectInfoDto("meishu", "美术", 3, 1));
					subjectInfos.add(new SubjectInfoDto("xinxi", "信息", 3, 1));
					subjectInfos.add(new SubjectInfoDto("tuanhuo", "团活", 3, 1));

					gi.setSubjectInfos(subjectInfos);
					for (Integer classNo = 0; classNo < gi.getClassCount(); classNo++) {
						ClassInfo classInfo = new ClassInfo();
						classInfo.setClassName(classNo.toString());
						//课程-教师映射
						Map<SubjectInfoDto, String> subjectTeachers = new HashMap<SubjectInfoDto, String>();
//                switch (classNo){
//                    case 0:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"111");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"118");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"124");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"12");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"118");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"120");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"111");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"118");
//                        break;
//                    case 1:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"112");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"119");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"124");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"123");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"118");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"112");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"119");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"112");
//                        break;
//                    case 2:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"113");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"120");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"124");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"123");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"118");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"120");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"120");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"120");
//                        break;
//                    case 3:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"114");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"119");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"125");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"123");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"118");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"120");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"119");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"114");
//                        break;
//                    case 4:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"115");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"121");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"125");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"123");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"115");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"121");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"106");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"115");
//                        break;
//                    case 5:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"116");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"122");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"18");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"113");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"54");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"120");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"122");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"116");
//                        break;
//                    case 6:
//                        subjectTeachers.put(new SubjectInfo("yw", "语文", 1, 9),"117");
//                        subjectTeachers.put(new SubjectInfo("sx", "数学", 1, 4),"121");
//                        subjectTeachers.put(new SubjectInfo("dd", "道德", 3, 3),"121");
//                        subjectTeachers.put(new SubjectInfo("ty", "体育", 3, 4),"27");
//                        subjectTeachers.put(new SubjectInfo("yy", "音乐", 3, 2),"54");
//                        subjectTeachers.put(new SubjectInfo("kx", "科学", 3, 1),"120");
//                        subjectTeachers.put(new SubjectInfo("ms", "美术", 3, 2),"117");
//                        subjectTeachers.put(new SubjectInfo("dh", "队会", 3, 1),"117");
//                        break;
//                }
                          /*
                            数学  5、英语 4、语文 5、音乐 1、体育 1、劳动 1、生物  2、地理 2、研学 1、历史 2、
                            道德 2、国学 1、健康 1、专题 1、实践 1、足球 1、美术 1、信息 1、团体活动 1
                          */
						switch (classNo) {
							case 0:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "舒丽军");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "邹华阳");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "谭文英");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "舒丽军");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "张小琅");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "屈超");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "张小琅");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "杨红霞");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "贺芊红");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "吉思瑶");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "谭文英");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "屈超");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "舒艳华");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "吉思瑶");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "彭壹欣");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "邹华阳");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "邹华阳");
								break;
							case 1:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "舒振鑫");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "邹华阳");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "贺秀华");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "舒丽军");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "张小琅");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "屈超");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "李芬");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "杨红霞");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张小琅");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "吉思瑶");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "贺秀华");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "屈超");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "张清华");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "舒振鑫");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "彭壹欣");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "邹华阳");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "李芬");
								break;
							case 2:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "胡晓春");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "张竹莲");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "杨必先");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "荆武");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "胡晓春");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "杨红霞");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "雷赛英");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "杨庚生");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "杨必先");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "张清华");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "舒振鑫");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "彭壹欣");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "张竹莲");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "张竹莲");
								break;
							case 3:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "胡晓春");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "张竹莲");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "刘青");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "荆武");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "胡晓春");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "屈超");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "王淳");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "赵友兴");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "金庆燊");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "向文君");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "刘青");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "屈超");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "周晶");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "王淳");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "肖丽娜");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "张竹莲");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "刘青");
								break;
							case 4:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "舒丽军");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "蔡春芳");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "刘青");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "舒丽军");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "向文君");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "梁文");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "李芬");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "张秋华");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "金庆燊");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "向文君");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "刘青");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "梁文");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "李燕");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "李芬");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "彭壹欣");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "蔡春芳");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "蔡春芳");
								break;
							case 5:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "林珍和");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "蔡春芳");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "谭文英");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "荆武");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "林珍和");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "梁文");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "张秋华");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "刘娟妮");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "杨庚生");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "谭文英");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "梁文");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "舒艳华");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "肖丽娜");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "蔡春芳");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "刘娟妮");
								break;
							case 6:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "舒彩英");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "周丹");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "杨玉");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "荆武");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "舒彩英");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "梁文");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "金庆燊");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "向文君");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "杨玉");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "梁文");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "张清华");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "王淳");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "彭壹欣");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "周丹");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "向文君");
								break;
							case 7:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "周芝兰");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "杨玉");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "贺荐");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "李芬");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张小琅");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "舒林华");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "杨玉");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "贺荐");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "李芬");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "周芝兰");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "张小琅");
								break;
							case 8:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "周芝兰");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "贺秀华");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "荆武");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "李芬");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "谌芳莲");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张小琅");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "舒林华");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "贺秀华");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "李芬");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "周芝兰");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "易庆云");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "贺秀华");
								break;
							case 9:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "林珍和");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "周丹");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "杨必先");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "贺慧梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "周丹");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "谌芳莲");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "雷赛英");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "舒林华");
								subjectTeachers.put(new SubjectInfoDto("guoxue", "国学", 3, 1), "杨必先");
								subjectTeachers.put(new SubjectInfoDto("jiankang", "健康", 3, 1), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "李芬");
								subjectTeachers.put(new SubjectInfoDto("zuqiu", "足球", 3, 1), "张倍彰");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "彭壹欣");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "周丹");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "林珍和");
								break;

						}
						classInfo.setSubjectTeachers(subjectTeachers);
						classInfos.put(classNo, classInfo);
					}
					gi.setClassInfos(classInfos);
					break;
				//一年级
				case 1:
					gi = new GradeInfoDto(grade, grade.toString(), 11, 5, 7, 4, 3, 7);
                    /*
                    数学  张睿  5
                    语文  舒圆  5
                    英语  吴晓琴 4
                    物理  刘兴仲  2
                    研学  吴晓琴  1
                    地理  宋颜雍   2
                    道德  戴焕喜  2
                    历史  李绍勤  2
                    体育  向华   2
                    美术  刘兴仲  1
                    音乐  陆妮曼 1
                    信息  张睿 1
                    实践  陆妮曼 1
                    劳动  舒圆 1
                    专题  宋颜雍 1
                    生物  陆妮曼 2
                    团活  吴晓琴 1
                     */
					subjectInfos.add(new SubjectInfoDto("shuxue", "数学", 1, 5));
					subjectInfos.add(new SubjectInfoDto("yingyu", "英语", 1, 4));
					subjectInfos.add(new SubjectInfoDto("yuwen", "语文", 1, 5));
					subjectInfos.add(new SubjectInfoDto("yinyue", "音乐", 3, 1));
					subjectInfos.add(new SubjectInfoDto("tiyu", "体育", 3, 2));
					subjectInfos.add(new SubjectInfoDto("laodong", "劳动", 3, 1));
					subjectInfos.add(new SubjectInfoDto("shengwu", "生物", 2, 2));
					subjectInfos.add(new SubjectInfoDto("wuli", "物理", 2, 2));
					subjectInfos.add(new SubjectInfoDto("dili", "地理", 2, 2));
					subjectInfos.add(new SubjectInfoDto("yanxue", "研学", 3, 1));
					subjectInfos.add(new SubjectInfoDto("lishi", "历史", 2, 2));
					subjectInfos.add(new SubjectInfoDto("daode", "道德", 3, 2));
					subjectInfos.add(new SubjectInfoDto("zhuanti", "专题", 3, 1));
					subjectInfos.add(new SubjectInfoDto("shijian", "实践", 3, 1));
					subjectInfos.add(new SubjectInfoDto("meishu", "美术", 3, 1));
					subjectInfos.add(new SubjectInfoDto("xinxi", "信息", 3, 1));
					subjectInfos.add(new SubjectInfoDto("tuanhuo", "团活", 3, 1));

					gi.setSubjectInfos(subjectInfos);
					for (Integer classNo = 0; classNo < gi.getClassCount(); classNo++) {
						ClassInfo classInfo = new ClassInfo();
						classInfo.setClassName(classNo.toString());
						Map<SubjectInfoDto, String> subjectTeachers = new HashMap<SubjectInfoDto, String>();
						switch (classNo) {
							case 0:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "张睿");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "吴晓琴");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "舒圆");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "陆妮曼");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "舒圆");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "陆妮曼");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "刘兴仲");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "吴晓琴");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "李绍勤");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "戴焕喜");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "陆妮曼");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "刘兴仲");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "张睿");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "吴晓琴");
								break;
							case 1:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "童达月");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "吴晓琴");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "舒圆");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "陆妮曼");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "舒圆");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "陆妮曼");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "刘兴仲");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "吴晓琴");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "李绍勤");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "戴焕喜");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "宋颜雍");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "陆妮曼");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "刘兴仲");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "童达月");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "舒圆");
								break;
							case 2:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "罗平");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "杨啸");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "杨啸");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "黄茜");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "李绍勤");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "戴焕喜");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "黄茜");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "罗平");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "罗平");
								break;
							case 3:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "罗平");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "贺美清");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "贺美清");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "钟光启");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "李绍勤");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "戴焕喜");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "刘让泉");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "钟光启");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "罗平");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "李绍勤");
								break;
							case 4:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "贺幸兴");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "卢慧");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "张美华");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "张美华");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "钟光启");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "樊必有");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "樊必有");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "李绍勤");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "肖勤慧");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "卢慧");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "贺幸兴");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "钟光启");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "雷爱梅");
								break;
							case 5:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "贺幸兴");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "肖勤慧");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "张美华");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "罗飞");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "张美华");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "钟光启");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "樊必有");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "樊必有");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张咪");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "肖勤慧");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "贺幸兴");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "钟光启");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "贺幸兴");
								break;
							case 6:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "杨芳");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "姜芳");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "贺美清");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "罗飞");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "贺美清");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "赵友兴");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "陈海艳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张咪");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "肖勤慧");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "雷爱梅");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "赵友兴");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "杨芳");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "赵友兴");
								break;
							case 7:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "杨芳");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "姜芳");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "向蓉华");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "丁爱华");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "罗飞");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "向蓉华");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "赵友兴");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "丁爱华");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "姜芳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张咪");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "肖勤慧");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "姜芳");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "赵友兴");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "杨芳");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "姜芳");
								break;
							case 8:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "童达月");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "卢慧");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "向蓉华");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "丁爱华");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "向蓉华");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "丁爱华");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张咪");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "肖勤慧");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "卢慧");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "卢慧");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "童达月");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "向蓉华");
								break;
							case 9:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "王渊");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "李燕");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "贾缓");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "罗飞");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "贾缓");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "黄湘芝");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "李绍勤");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "吉思瑶");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "黄湘芝");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "王渊");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "李燕");
								break;
							case 10:
								subjectTeachers.put(new SubjectInfoDto("shuxue", "数学", 1, 5), "王渊");
								subjectTeachers.put(new SubjectInfoDto("yingyu", "英语", 1, 4), "李燕");
								subjectTeachers.put(new SubjectInfoDto("yuwen", "语文", 1, 5), "贾缓");
								subjectTeachers.put(new SubjectInfoDto("yinyue", "音乐", 3, 1), "刘斐斌");
								subjectTeachers.put(new SubjectInfoDto("tiyu", "体育", 3, 2), "向华");
								subjectTeachers.put(new SubjectInfoDto("laodong", "劳动", 3, 1), "贾缓");
								subjectTeachers.put(new SubjectInfoDto("shengwu", "生物", 2, 2), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("wuli", "物理", 2, 2), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("dili", "地理", 2, 2), "黄湘芝");
								subjectTeachers.put(new SubjectInfoDto("yanxue", "研学", 3, 1), "尹菊芳");
								subjectTeachers.put(new SubjectInfoDto("lishi", "历史", 2, 2), "张咪");
								subjectTeachers.put(new SubjectInfoDto("daode", "道德", 3, 2), "吉思瑶");
								subjectTeachers.put(new SubjectInfoDto("zhuanti", "专题", 3, 1), "蔡桔");
								subjectTeachers.put(new SubjectInfoDto("shijian", "实践", 3, 1), "刘慈姣");
								subjectTeachers.put(new SubjectInfoDto("meishu", "美术", 3, 1), "黄湘芝");
								subjectTeachers.put(new SubjectInfoDto("xinxi", "信息", 3, 1), "王渊");
								subjectTeachers.put(new SubjectInfoDto("tuanhuo", "团活", 3, 1), "王渊");
								break;
						}
						classInfo.setSubjectTeachers(subjectTeachers);
						classInfos.put(classNo, classInfo);
					}
					gi.setClassInfos(classInfos);
					break;
			}
			gradeInfo.put(grade, gi);
		}
		List<SchedulePositionInfo> fixedInfos = new ArrayList<SchedulePositionInfo>();
		for (Integer idx = 0; idx < gradeInfo.size(); idx++) {
			for (Integer c = 0; c < gradeInfo.get(idx).getClassCount(); c++) {
				fixedInfos.add(new SchedulePositionInfo(idx, c, 4, 6, new SubjectInfoDto("tuanhuo", "团活", 3, 1), ""));
			}
		}
//        fixedInfos.add(new SchedulePositionInfo(0, 0, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 1, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 2, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 3, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 4, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 5, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 6, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 7, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 8, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));
//        fixedInfos.add(new SchedulePositionInfo(0, 9, 4, 6, new SubjectInfo("tuanhuo", "团活", 3, 1), ""));

//        fixedInfos.add(new SchedulePositionInfo(0,0,4,5,new SubjectInfo("dh", "队会", 3, 1),""));
//        fixedInfos.add(new SchedulePositionInfo(0,1,4,5,new SubjectInfo("dh", "队会", 3, 1),""));
//        fixedInfos.add(new SchedulePositionInfo(0,2,4,5,new SubjectInfo("dh", "队会", 3, 1),""));
//        fixedInfos.add(new SchedulePositionInfo(0,3,4,5,new SubjectInfo("dh", "队会", 3, 1),""));
//        fixedInfos.add(new SchedulePositionInfo(0,4,4,5,new SubjectInfo("dh", "队会", 3, 1),""));
//        fixedInfos.add(new SchedulePositionInfo(0,5,4,5,new SubjectInfo("dh", "队会", 3, 1),""));
//        fixedInfos.add(new SchedulePositionInfo(0,6,4,5,new SubjectInfo("dh", "队会", 3, 1),""));


		classScheduling.setUdfFixedSubjectList(fixedInfos);//外部初始化
		classScheduling.setGradeInfoMap(gradeInfo);
		classScheduling.makeLessonTable();//开始制作课程表
		for (Map.Entry<Integer, Map<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>>> item : classScheduling.getScheduleInfos().entrySet()) {
			for (Map.Entry<Integer, Map<Integer, Map<Integer, SchedulePositionInfo>>> itemItem : item.getValue().entrySet()) {
				System.out.println("_________________" + item.getKey().toString() + "年级" + itemItem.getKey().toString() + "班__________");
				for (Map.Entry<Integer, Map<Integer, SchedulePositionInfo>> itemItemItem : itemItem.getValue().entrySet()) {
					System.out.print("周" + itemItemItem.getKey().toString() + "  ");
					for (Map.Entry<Integer, SchedulePositionInfo> itemItemItemItem : itemItemItem.getValue().entrySet()) {
						if (itemItemItemItem.getValue().getSubjectInfoDto() != null) {
							System.out.print(itemItemItemItem.getValue().getSubjectInfoDto().getSubjectName() + "(" + fillStringLength(itemItemItemItem.getValue().getTeacher().toString(), 3) + ")   ");
						} else {
							System.out.print("--  ");
						}
					}
					System.out.println();
				}
			}
		}

	}

	//对齐处理
	private static String fillStringLength(String value, Integer len) {
		String result = value;
		if (value.length() < len) {
			for (Integer l = 0; l < len - value.length(); l++) {
				result += "  ";
			}
		}
		return result;
	}

}
