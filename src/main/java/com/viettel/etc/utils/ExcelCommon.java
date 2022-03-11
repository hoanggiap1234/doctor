package com.viettel.etc.utils;

import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.ExcellDataEntity;
import com.viettel.etc.xlibrary.core.entities.ExcellHeaderEntity;
import com.viettel.etc.xlibrary.core.entities.ExcellSheet;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Method common for export excel
 */
public class ExcelCommon extends FnCommon {

	public static final String DELIMITER_MERGE_CELL_HEADER = ":";
	public static final String NEW_LINE = "\n";
	public static final String STRING_EMPTY = "";
	private static final Logger LOGGER = Logger.getLogger(ExcelCommon.class);

	/**
	 * export excel file with path
	 * Merge cell header with DELIMITER_MERGE_CELL_HEADER
	 * Example list header with merge : test:test1, test:test2
	 * list header without merge test:test, STT:STT
	 *
	 * @param sheetExport ExcellSheet export
	 * @param pathFile    file to export
	 * @param strTitle    title
	 * @return Path to file export
	 */
	public static Optional<Path> exportExcel(ExcellSheet sheetExport, String pathFile, String strTitle) {
		try {
			Workbook workbook = new XSSFWorkbook();

			XSSFFont font = ((XSSFWorkbook) workbook).createFont();
			ExcelCommon.setStyleFont(font);

			XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
			ExcelCommon.setHeaderStyle(headerStyle);
			headerStyle.setFont(font);

			XSSFCellStyle rowIndexHeaderStyle = (XSSFCellStyle) workbook.createCellStyle();
			ExcelCommon.setRowIndexHeaderStyle(rowIndexHeaderStyle);

			XSSFCellStyle headerStyleHeader = (XSSFCellStyle) workbook.createCellStyle();
			ExcelCommon.setStyleTitle(headerStyleHeader);
			headerStyleHeader.setFont(font);

			String strNameSheet = sheetExport.getStrSheetName() != null && sheetExport.getStrSheetName().trim().length() > 0 ? sheetExport.getStrSheetName() : "Sheet1";
			Sheet sheet = workbook.createSheet(strNameSheet);

			List<ExcellHeaderEntity> listHeader = sheetExport.getListHeader();

			for (int i = 0; i < listHeader.size(); i++) {
				int columnWidth = Integer.valueOf(0).compareTo(listHeader.get(i).getWidth()) < 0 ? listHeader.get(i).getWidth() : 6000;
				sheet.setColumnWidth(i, columnWidth);
			}

			// start row
			int rowsIndex = 0;

			if (!StringUtils.isEmpty(strTitle)) {
				int firstCol = 0;
				for (String str : strTitle.trim().split("\r\n|\r|\n")) {
					Row rowTitle = sheet.createRow(++rowsIndex);
					Cell cellHeader = rowTitle.createCell(firstCol);
					cellHeader.setCellValue(str);
					cellHeader.setCellStyle(headerStyleHeader);
					sheet.addMergedRegion(new CellRangeAddress(rowsIndex, rowsIndex, firstCol, listHeader.size() - 1));
				}
				// next row empty
				rowsIndex++;
			}

			// row header
			Row headerTop = sheet.createRow(++rowsIndex);
			int startRowBordersToMergedCell = rowsIndex;
			List<String> cellValuesRowTop = listHeader.stream().map(excelHeaderEntity -> excelHeaderEntity.getHeaderName().split(DELIMITER_MERGE_CELL_HEADER)[0]).collect(Collectors.toList());
			int size = listHeader.size();
			int j = 0;
			while (j < size) {
//			for (int j = 0; j < listHeader.size(); j++) {
				Cell headerCell = headerTop.createCell(j);
				String cellValue = listHeader.get(j).getHeaderName();
				String[] nameArr = cellValue.split(DELIMITER_MERGE_CELL_HEADER);
				if (nameArr.length == 2) {
					if (nameArr[0].equalsIgnoreCase(nameArr[1])) {
						sheet.addMergedRegion(new CellRangeAddress(rowsIndex, rowsIndex + 1, j, j));
					} else {
						Long numberColumnMerge = cellValuesRowTop.stream().filter(value -> value.equalsIgnoreCase(nameArr[0])).count();
						int nextColumn = j + numberColumnMerge.intValue() - 1;
						sheet.addMergedRegion(new CellRangeAddress(rowsIndex, rowsIndex, j, nextColumn));
						// next column
						j = nextColumn;
					}
				} else {
					j++;
				}
				headerCell.setCellValue(nameArr[0]);
				headerCell.setCellStyle(headerStyle);
			}

			// create row bottom
			if (listHeader.stream().anyMatch(excelHeaderEntity -> excelHeaderEntity.getHeaderName().split(DELIMITER_MERGE_CELL_HEADER).length >= 2)) {
				Row headerLast = sheet.createRow(++rowsIndex);
				for (int i = 0; i < listHeader.size(); i++) {
					String cellValue = listHeader.get(i).getHeaderName();
					String[] nameArr = cellValue.split(DELIMITER_MERGE_CELL_HEADER);
					if (!nameArr[0].equalsIgnoreCase(nameArr[1])) {
						Cell headerCell = headerLast.createCell(i);
						headerCell.setCellValue(nameArr[1]);
						headerCell.setCellStyle(headerStyle);
					}
				}
			}

			// create row index
			Row rowIndexNumber = sheet.createRow(++rowsIndex);
			for (int i = 0; i < listHeader.size(); i++) {
				Cell cell = rowIndexNumber.createCell(i);
				cell.setCellValue(MessageFormat.format("({0})", i + 1));
				cell.setCellStyle(rowIndexHeaderStyle);
			}

			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			ExcelCommon.setStyleCellData(style);

			ExcellDataEntity listData = sheetExport.getExcellDataEntity();

			for (int j1 = 0; j1 < listData.getListData().size(); ++j1) {
				List<Object> listObjectRow = listData.getListData().get(j1);
				Row row = sheet.createRow(++rowsIndex);

				for (int k = 0; k < listObjectRow.size(); ++k) {
					Cell cell = row.createCell(k);
					if (checkIsNumber(listObjectRow.get(k))) {
						cell.setCellValue(Double.valueOf(listObjectRow.get(k).toString()));
					} else if (listObjectRow.get(k) == null) {
						cell.setCellValue(STRING_EMPTY);
					} else {
						cell.setCellValue(String.valueOf(listObjectRow.get(k)));
					}

					cell.setCellStyle(style);
				}
			}

			ExcelCommon.setBordersToMergedCells(sheet, startRowBordersToMergedCell);
			FileOutputStream outputStream = new FileOutputStream(pathFile);
			workbook.write(outputStream);
			workbook.close();

			Path path = Paths.get(pathFile);
			return Optional.ofNullable(path);
		} catch (IOException var19) {
			LOGGER.error("exportFileExcel error : " + var19.getMessage(), var19);
			return Optional.ofNullable(null);
		}
	}

	public static void setStyleFont(XSSFFont font) {
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 14);
		font.setBold(true);
	}

	public static void setStyleCellData(CellStyle cellStyle) {
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		ExcelCommon.setStyleBorder(cellStyle);
	}

	public static void setStyleTitle(CellStyle headerStyleHeader) {
		headerStyleHeader.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		headerStyleHeader.setFillPattern(FillPatternType.NO_FILL);

		headerStyleHeader.setAlignment(HorizontalAlignment.CENTER);
		headerStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);

		headerStyleHeader.setWrapText(true);
	}

	public static void setRowIndexHeaderStyle(CellStyle cellStyle) {
		ExcelCommon.setHeaderStyle(cellStyle);
		((XSSFCellStyle) cellStyle).setFillForegroundColor(ExcelCommon.getRgbForeground("ecf0e1"));
	}

	public static void setHeaderStyle(CellStyle cellStyle) {
		((XSSFCellStyle) cellStyle).setFillForegroundColor(ExcelCommon.getRgbForeground("dceef4"));
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		ExcelCommon.setStyleBorder(cellStyle);
	}

	public static void setStyleBorder(CellStyle cellStyle) {
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
	}

	public static void setBordersToMergedCells(Sheet sheet, int startRowBordersToMergedCell) {
		sheet.getMergedRegions().stream().filter(rangeAddress -> rangeAddress.getFirstRow() >= startRowBordersToMergedCell).forEach(rangeAddress -> {
			RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
			RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
		});
	}

	public static XSSFColor getRgbForeground(String rgbHex) {
		try {
			byte[] rgbB = Hex.decodeHex(rgbHex);
			XSSFColor xSSFColor = new XSSFColor(rgbB, null);
			return xSSFColor;
		} catch (DecoderException e) {
			LOGGER.info(e);
			return new XSSFColor(IndexedColors.WHITE, null);
		}
	}

	public static Boolean checkIsNumber(Object o) {
		return o instanceof Integer || o instanceof Double || o instanceof Float || o instanceof Long;
	}

	public static ResponseEntity<Object> getExportExcelResponseEntity(Optional<Path> pathOpt) {
		if (!pathOpt.isPresent()) {
			LOGGER.debug("Download file error");
			return new ResponseEntity<>(FunctionCommon.responseToClient("FILE NOT FOUND"), HttpStatus.NOT_FOUND);
		}

		try {
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(pathOpt.get()));

			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pathOpt.get().getFileName());
			header.add("Access-Control-Expose-Headers", "Content-Disposition");

			return ResponseEntity.ok().headers(header).contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (IOException e) {
			LOGGER.error("Download file error", e);
			return new ResponseEntity<>(FunctionCommon.responseToClient("FILE NOT FOUND"), HttpStatus.NOT_FOUND);
		}
	}

}
