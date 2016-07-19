package com.tts.zhanwai.scheduled;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tts.zhanwai.BootApp;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(BootApp.class)
public class ScheduledTasksTest {
	@Rule
	public OutputCapture outputCapture = new OutputCapture();
	@Autowired
	private ScheduledTasks scheduledTasks;

	@Test
	public void testScheduledTasks() {
		scheduledTasks.startScheduledTask();
	}
}
