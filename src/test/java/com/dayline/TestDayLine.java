package com.dayline;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gupiao.dao.DayLineDao;
import com.gupiao.model.DayLine;

public class TestDayLine {

	@Test
	public void test() {
		DayLine dayLine=new DayLineDao().getLastDayLine("600000");
		System.out.println(dayLine);
	}

}
