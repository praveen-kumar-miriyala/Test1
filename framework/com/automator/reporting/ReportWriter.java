package com.automator.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.automator.reporting.ReportingVariables.Status;
import com.automator.utils.PropManager;

public class ReportWriter {

	TestSuite reportSuite;

	public ReportWriter(TestSuite reportSuite) {
		this.reportSuite = reportSuite;
	}

	public void writeReport() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(writeSuiteHeader());
		sb.append(writeTestDetails());
		sb.append(closeReport());
		String repFolder=PropManager.getAbsolutPath("ReportStore");
		File repFolderObj = new File(repFolder);
		if (!repFolderObj.exists()) {
			repFolderObj.mkdirs();
		}
		File repName = new File(repFolder +"\\" + reportSuite.getName() + "_" + PropManager.getTimeStamp() + ".html");

		FileWriter repWriter = new FileWriter(repName);
		repWriter.write(sb.toString());
		repWriter.close();
	}

	private String writeSuiteHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head>");
		sb.append("<script>");
		sb.append("function expand(itemId) { document.getElementById(itemId).style.display = 'block'; } ");
		sb.append("function collapse(itemId) { document.getElementById(itemId).style.display = 'none'; }");
		sb.append("</script></head>");
		sb.append("<body style='font-family:verdana;background:#d7f2dd'>");
		sb.append(
				"<div style='width:100%;text-align:center;vertical-align:middle;font-size:14pt;font-weight:bold;height:30px;background:#36bfdb'>"
						+ reportSuite.getName().toUpperCase() + " - AUTOMATION REPORT</div>");
		sb.append(addSeperator(5));
		sb.append("<table border='0' style='width:100%'>");
		sb.append("<tr>");
		sb.append(addHeaderColumn("Start Time", 1, "center"));
		sb.append(addHeaderColumn("End Time", 1, "center"));
		sb.append(addHeaderColumn("Total Duration", 1, "center"));
		sb.append(addHeaderColumn("Executed", 1, "center"));
		sb.append(addHeaderColumn("Passed", 1, "center"));
		sb.append(addHeaderColumn("Failed", 1, "center"));
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append(addColumn(reportSuite.getStartTime().toString(), 1, "center"));
		sb.append(addColumn(reportSuite.getEndTime().toString(), 1, "center"));
		long diffInMillies = Math.abs(reportSuite.getEndTime().getTime() - reportSuite.getStartTime().getTime());
	    long duration = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		sb.append(addColumn(Long.toString(duration) + " Secs", 1, "center"));
		sb.append(addColumn(Integer.toString(reportSuite.getTotalTests()), 1, "center"));
		sb.append(addColumn(Integer.toString(reportSuite.getPassedTests()), 1, "center"));
		sb.append(addColumn(Integer.toString(reportSuite.getFailedTests()), 1, "center"));
		sb.append("</tr>");
		sb.append("</table>");
		sb.append(addSeperator(5));
		return sb.toString();
	}

	private String addSeperator(int height) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='height:" + Integer.toString(height) + "px' ></div>");
		return sb.toString();
	}

	private String addHeaderColumn(String data, int colSpan, String align) {
		if (null == align || align.isEmpty()) {
			align = "left";
		}
		StringBuilder sbCol = new StringBuilder();
		sbCol.append("<td colspan='" + colSpan + "' style='font-weight:bold;text-align:" + align
				+ ";font-size:10pt;background:#225e6b;color:white;'>");
		sbCol.append(getData(data));
		sbCol.append("</td>");
		return sbCol.toString();
	}

	private String addModuleHeaderColumn(String data, int colSpan, String align) {
		if (null == align || align.isEmpty()) {
			align = "left";
		}
		StringBuilder sbCol = new StringBuilder();
		sbCol.append("<td colspan='" + colSpan + "' style='font-weight:bold;text-align:" + align
				+ ";font-size:10pt;background:#cbdced;color:white;'>");
		sbCol.append(getData(data));
		sbCol.append("</td>");
		return sbCol.toString();
	}

	private String addColumn(String data, int colSpan, String align) {
		if (null == align || align.isEmpty()) {
			align = "left";
		}

		StringBuilder sbCol = new StringBuilder();
		sbCol.append(
				"<td colspan='" + colSpan + "' style='font-size:9pt;background:#c3e5db;text-align:" + align + "'>");
		sbCol.append(data);
		sbCol.append("</td>");
		return sbCol.toString();
	}

	private String addButtonColumn(String expandKey, int colSpan, String align) {
		if (null == align || align.isEmpty()) {
			align = "left";
		}
		StringBuilder sbCol = new StringBuilder();
		sbCol.append(
				"<td colspan='" + colSpan + "' style='font-size:9pt;background:#c3e5db;text-align:" + align + "'>");
		sbCol.append("<button onclick=\"expand('" + expandKey.trim()
				+ "')\" style='font-weight:bold'>+</button><span style='width:5px'></span><button onclick=\"collapse('"
				+ expandKey.trim() + "')\" style='font-weight:bold'>-</button>");
		sbCol.append("</td>");
		return sbCol.toString();
	}

	private String getData(String input) {
		if (null == input) {
			input = "";
		}
		// input = "<div style='width:100%;height:100%;border:1px solid
		// lightblue;'>" + input + "</div>";
		return input;
	}

	private String writeTestDetails() {
		StringBuilder sbTCHeader = new StringBuilder();
		sbTCHeader.append("<table border='0'  style='width:100%'>");
		sbTCHeader.append("<tr>");
		sbTCHeader.append(addHeaderColumn("Exp/Coll", 1, "center"));
		sbTCHeader.append(addHeaderColumn("Seq", 1, "center"));
		sbTCHeader.append(addHeaderColumn("Test Name", 2, null));
		sbTCHeader.append(addHeaderColumn("Duration", 1, "center"));
		sbTCHeader.append(addHeaderColumn("Iteration", 1, "center"));
		sbTCHeader.append(addHeaderColumn("Status", 1, "center"));
		sbTCHeader.append("</tr>");
		int testCounter = 0;
		for (TestCase test : reportSuite.getLsTests()) {
			testCounter++;
			sbTCHeader.append("<tr>");
			sbTCHeader.append(addButtonColumn("tc" + testCounter, 1, "center"));
			sbTCHeader.append(addColumn(Integer.toString(testCounter), 1, "center"));
			sbTCHeader.append(addColumn(test.getName(), 2, null));
			long diffInMillies = Math.abs(test.getEndTime().getTime() - test.getStartTime().getTime());
		    long duration = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			sbTCHeader.append(addColumn(Long.toString(duration)+ " Secs", 1, "center"));
			sbTCHeader.append(addColumn(Integer.toString(test.getIteration()), 1, "center"));
			sbTCHeader.append(addStatusColumn(test.getStatus(), 1, ""));
			sbTCHeader.append("</tr>");

			sbTCHeader.append("<tr><td  colspan='7' style='width:100%'>");
			sbTCHeader.append("<div id='tc" + testCounter + "' style='display: none;border: 1px solid red;'>");
			sbTCHeader.append("<table border='1'  style='width:100%'>");
			sbTCHeader.append(addStepHeader());
			int stepSeq = 1;
			for (Step step : test.getLsSteps()) {
				sbTCHeader.append(addStepDetail(step, stepSeq));
				if (!step.isModule()) {
					stepSeq++;
				}
			}
			sbTCHeader.append("</div>");
			sbTCHeader.append("</table>");
			sbTCHeader.append("</td></tr>");

		}
		sbTCHeader.append("</table>");
		return sbTCHeader.toString();
	}

	private String addStepHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append(addHeaderColumn("Seq", 1, "center"));
		sb.append(addHeaderColumn("Step", 1, null));
		sb.append(addHeaderColumn("Data", 1, null));
		sb.append(addHeaderColumn("Status", 1, "center"));
		sb.append(addHeaderColumn("Remarks", 1, null));
		sb.append(addHeaderColumn("Duration", 1, "center"));
		sb.append(addHeaderColumn("Screenshot", 1, null));
		sb.append("</tr>");
		return sb.toString();
	}

	private static String formatAutoModuleName(String inputString) {
		String retString = "";
		for (char nameChar : inputString.toCharArray()) {
			if (Character.isUpperCase(nameChar)) {
				retString += " ";
			}
			if (retString.isEmpty()) {
				retString += Character.toString(nameChar).toUpperCase();
			} else {
				retString += Character.toString(nameChar);
			}
		}
		return retString;
	}

	private String addStepDetail(Step step, int seq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		if (step.isModule()) {
			sb.append(addModuleHeaderColumn(formatAutoModuleName(step.getName()), 5, null));
			sb.append(addStatusColumn(step.getStatus(), 2, "Module"));
		} else {
			sb.append(addColumn(Integer.toString(seq), 1, "center"));
			sb.append(addColumn(step.getName(), 1, null));
			sb.append(addColumn(step.getData(), 1, null));
			sb.append(addStatusColumn(step.getStatus(), 1, ""));
			sb.append(addColumn(step.getActualResults(), 1, null));
			sb.append(addColumn("0", 1, "center"));
			if (null != step.getScreenshot() && !step.getScreenshot().isEmpty()) {
				sb.append(addColumn("<a href='" + step.getScreenshot()
						+ "' target='_blank'><img style='height:25px;width:25px;' src='" + step.getScreenshot()
						+ "' /></a>", 1, null));
			} else {
				sb.append(addColumn("", 1, null));
			}
		}
		sb.append("</tr>");
		return sb.toString();
	}

	private String addStatusColumn(Status status, int colSpan, String backType) {
		StringBuilder sbStatCol = new StringBuilder();
		String background = "#c3e5db";
		String foreground = "black";

		switch (backType) {
		case "Test":
			background = "#6b6022";
			break;
		case "Step":
			background = "#225e6b";
			break;
		case "Module":
			background = "#cbdced";
			break;
		default:
			background = "#c3e5db";
			break;
		}

		if (null == status) {
			status = Status.Done;
		}

		if (status == Status.Failed) {
			// background = "red";
			// foreground = "white";
			foreground = "red";
		} else if (status == Status.Passed) {
			// background = "green";
			// foreground = "white";
			foreground = "green";
		}

		sbStatCol.append(
				"<td colspan='" + colSpan + "' style='font-weight:bold;text-align:center;font-size:10pt;background:"
						+ background + ";color:" + foreground + "'>");
		sbStatCol.append(getData(status.toString()));
		sbStatCol.append("</td>");
		return sbStatCol.toString();
	}

	private String closeReport() {
		StringBuilder sb = new StringBuilder();
		sb.append("</body></html>");
		return sb.toString();
	}

}
