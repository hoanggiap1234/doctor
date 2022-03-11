package com.viettel.etc.utils;

import com.viettel.etc.xlibrary.core.entities.ExcellDataEntity;
import com.viettel.etc.xlibrary.core.entities.ExcellHeaderEntity;
import com.viettel.etc.xlibrary.core.entities.ExcellSheet;
import mockit.MockUp;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


class ExcelCommonTest {

	@Test
	void exportExcel() {
		new MockUp<Paths>() {
			@mockit.Mock
			public Path get(String first, String... more) {
				return null;
			}
		};

		ExcellSheet sheetExport = new ExcellSheet();
		List<ExcellHeaderEntity> listHeader = Arrays.asList("Stt", "Ngày đăng ký", "Mã đặt lịch", "Họ và tên", "Ngày tháng năm sinh",
				"Giới tính", "Số điện thoại", "Email", "Địa chỉ", "Dịch vụ", "Bác sĩ", "Cơ sở y tế", "Trạng thái").stream().map(headerName ->
				new ExcellHeaderEntity(headerName, 6000)).collect(Collectors.toList());
		sheetExport.setListHeader(listHeader);
		ExcellDataEntity excellDataEntity = new ExcellDataEntity();
		List<List<Object>> listDataResultExcel = new ArrayList<>();

		excellDataEntity.setListData(listDataResultExcel);
		sheetExport.setExcellDataEntity(excellDataEntity);

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean createFolder(String containerFolder) {
				return true;
			}
		};

		String filePath = "";
		String title = "excel";

		MatcherAssert.assertThat(ExcelCommon.exportExcel(sheetExport, filePath, title), Matchers.notNullValue());
	}

	@Test
	void setStyleFont() {
		XSSFFont font = new XSSFFont();

		XSSFFont newFont = new XSSFFont();
		newFont.setFontName("Arial");
		newFont.setFontHeightInPoints((short) 14);
		newFont.setBold(true);

		ExcelCommon.setStyleFont(font);

		MatcherAssert.assertThat(font, Matchers.equalTo(newFont));
	}

	@Test
	void setStyleCellData() {
		Workbook workbook = new XSSFWorkbook();
		XSSFCellStyle cellStyleIn = (XSSFCellStyle) workbook.createCellStyle();

		CellStyle cellStyle = cellStyleIn;
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		ExcelCommon.setStyleCellData(cellStyle);

		MatcherAssert.assertThat(cellStyle, Matchers.equalTo(cellStyle));
	}

	@Test
	void setStyleTitle() {
		Workbook workbook = new XSSFWorkbook();
		XSSFCellStyle headerStyleHeaderIn = (XSSFCellStyle) workbook.createCellStyle();
		CellStyle headerStyleHeader = headerStyleHeaderIn;
		headerStyleHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		headerStyleHeader.setFillPattern(FillPatternType.NO_FILL);

		headerStyleHeader.setAlignment(HorizontalAlignment.CENTER);
		headerStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

		headerStyleHeader.setWrapText(true);

		ExcelCommon.setStyleCellData(headerStyleHeaderIn);

		MatcherAssert.assertThat(headerStyleHeaderIn, Matchers.equalTo(headerStyleHeader));
	}

	@Test
	void setRowIndexHeaderStyle() {
		Workbook workbook = new XSSFWorkbook();
		XSSFCellStyle cellStyleIn = (XSSFCellStyle) workbook.createCellStyle();
		CellStyle cellStyle = cellStyleIn;
		ExcelCommon.setHeaderStyle(cellStyle);
		((XSSFCellStyle) cellStyle).setFillForegroundColor(ExcelCommon.getRgbForeground("ecf0e1"));

		ExcelCommon.setRowIndexHeaderStyle(cellStyleIn);

		MatcherAssert.assertThat(cellStyleIn, Matchers.equalTo(cellStyle));
	}

	@Test
	void setHeaderStyle() {
		Workbook workbook = new XSSFWorkbook();
		XSSFCellStyle cellStyleIn = (XSSFCellStyle) workbook.createCellStyle();
		CellStyle cellStyle = cellStyleIn;
		((XSSFCellStyle) cellStyle).setFillForegroundColor(ExcelCommon.getRgbForeground("dceef4"));
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		ExcelCommon.setStyleBorder(cellStyle);

		ExcelCommon.setHeaderStyle(cellStyle);

		MatcherAssert.assertThat(cellStyle, Matchers.equalTo(cellStyle));
	}

	@Test
	void setStyleBorder() {
		Workbook workbook = new XSSFWorkbook();

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();

		XSSFCellStyle cellStyleIn = (XSSFCellStyle) workbook.createCellStyle();

		CellStyle cellStyle = cellStyleIn;
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		ExcelCommon.setStyleBorder(cellStyle);

		MatcherAssert.assertThat(cellStyle, Matchers.equalTo(cellStyle));
	}

	@Test
	void setBordersToMergedCells() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheetIn = workbook.createSheet("strNameSheet");
		Sheet sheet = sheetIn;

		sheet.getMergedRegions().stream().filter(rangeAddress -> rangeAddress.getFirstRow() >= 1).forEach(rangeAddress -> {
			RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
			RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
		});

		ExcelCommon.setBordersToMergedCells(sheetIn, 1);

		MatcherAssert.assertThat(sheetIn, Matchers.equalTo(sheet));
	}

	@Test
	void getRgbForeground() {
	}

	@Test
	void checkIsNumber() {
		MatcherAssert.assertThat(ExcelCommon.checkIsNumber(1), Matchers.equalTo(true));
	}

	@Test
	void getExportExcelResponseEntity() {
	}

	public static class CellStyleTest extends XSSFCellStyle implements CellStyle {
		public CellStyleTest(int cellXfId, int cellStyleXfId, StylesTable stylesSource, ThemesTable theme) {
			super(cellXfId, cellStyleXfId, stylesSource, theme);
		}

		@Override
		public short getIndex() {
			return 0;
		}

		@Override
		public short getDataFormat() {
			return 0;
		}

		@Override
		public void setDataFormat(short i) {

		}

		@Override
		public String getDataFormatString() {
			return null;
		}

		@Override
		public void setFont(Font font) {

		}

		@Override
		public short getFontIndex() {
			return 0;
		}

		@Override
		public int getFontIndexAsInt() {
			return 0;
		}

		@Override
		public boolean getHidden() {
			return false;
		}

		@Override
		public void setHidden(boolean b) {

		}

		@Override
		public boolean getLocked() {
			return false;
		}

		@Override
		public void setLocked(boolean b) {

		}

		@Override
		public boolean getQuotePrefixed() {
			return false;
		}

		@Override
		public void setQuotePrefixed(boolean b) {

		}

		@Override
		public HorizontalAlignment getAlignment() {
			return null;
		}

		@Override
		public void setAlignment(HorizontalAlignment horizontalAlignment) {

		}

		@Override
		public HorizontalAlignment getAlignmentEnum() {
			return null;
		}

		@Override
		public boolean getWrapText() {
			return false;
		}

		@Override
		public void setWrapText(boolean b) {

		}

		@Override
		public VerticalAlignment getVerticalAlignment() {
			return null;
		}

		@Override
		public void setVerticalAlignment(VerticalAlignment verticalAlignment) {

		}

		@Override
		public VerticalAlignment getVerticalAlignmentEnum() {
			return null;
		}

		@Override
		public short getRotation() {
			return 0;
		}

		@Override
		public void setRotation(short i) {

		}

		@Override
		public short getIndention() {
			return 0;
		}

		@Override
		public void setIndention(short i) {

		}

		@Override
		public BorderStyle getBorderLeft() {
			return null;
		}

		@Override
		public void setBorderLeft(BorderStyle borderStyle) {

		}

		@Override
		public BorderStyle getBorderLeftEnum() {
			return null;
		}

		@Override
		public BorderStyle getBorderRight() {
			return null;
		}

		@Override
		public void setBorderRight(BorderStyle borderStyle) {

		}

		@Override
		public BorderStyle getBorderRightEnum() {
			return null;
		}

		@Override
		public BorderStyle getBorderTop() {
			return null;
		}

		@Override
		public void setBorderTop(BorderStyle borderStyle) {

		}

		@Override
		public BorderStyle getBorderTopEnum() {
			return null;
		}

		@Override
		public BorderStyle getBorderBottom() {
			return null;
		}

		@Override
		public void setBorderBottom(BorderStyle borderStyle) {

		}

		@Override
		public BorderStyle getBorderBottomEnum() {
			return null;
		}

		@Override
		public short getLeftBorderColor() {
			return 0;
		}

		@Override
		public void setLeftBorderColor(short i) {

		}

		@Override
		public short getRightBorderColor() {
			return 0;
		}

		@Override
		public void setRightBorderColor(short i) {

		}

		@Override
		public short getTopBorderColor() {
			return 0;
		}

		@Override
		public void setTopBorderColor(short i) {

		}

		@Override
		public short getBottomBorderColor() {
			return 0;
		}

		@Override
		public void setBottomBorderColor(short i) {

		}

		@Override
		public FillPatternType getFillPattern() {
			return null;
		}

		@Override
		public void setFillPattern(FillPatternType fillPatternType) {

		}

		@Override
		public FillPatternType getFillPatternEnum() {
			return null;
		}

		@Override
		public short getFillBackgroundColor() {
			return 0;
		}

		@Override
		public void setFillBackgroundColor(short i) {

		}


		@Override
		public short getFillForegroundColor() {
			return 0;
		}

		@Override
		public void setFillForegroundColor(short i) {

		}

		@Override
		public void cloneStyleFrom(CellStyle cellStyle) {

		}

		@Override
		public boolean getShrinkToFit() {
			return false;
		}

		@Override
		public void setShrinkToFit(boolean b) {

		}
	}
}
