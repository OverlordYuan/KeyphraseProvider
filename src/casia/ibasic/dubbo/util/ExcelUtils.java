package casia.ibasic.dubbo.util;

import com.alibaba.fastjson.JSONObject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel操作工具类
 * @author Overlord.Y
 */

public class ExcelUtils {

    public static List<String> readexcel(String filepath){
		ExcelUtils excel = new ExcelUtils();
		readfile rf = new readfile();
		List<JSONObject> files = rf.read(filepath);
		List<String> content = new ArrayList<String>();
		for(JSONObject file:files){
			try {
				content.addAll(excel.readColumn(file.get("path").toString(),2));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(content.size());
		return content;
	}

	public static List<String> readColumn(String file, int index) throws Exception {
		InputStream inputStream = new FileInputStream(file);
		Workbook workbook = Workbook.getWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		int columns = sheet.getColumns();
		List<String> content = new ArrayList<String>();
		for (int i = 1; i < rows; i++) {
			Cell cell = sheet.getCell(index,i);
			content.add(cell.getContents());
		}
		return content;
	}
	public static void exportExcel(String path, JSONObject list, String[] infoname){

		WritableWorkbook book = null;
		System.out.println(path);
//		String[] info = infoname;
		try{
			book = Workbook.createWorkbook(new File(path));
			//生成名为eccif的工作表，参数0表示第一页
			WritableSheet sheet = book.createSheet("0", 0);
			//表头导航
			for(int j=0;j<infoname.length;j++){
				Label label = new Label(j, 0, infoname[j]);
				sheet.addCell(label);
			}
			List len = (List) list.get(infoname[0]);
			for(int i=0;i<len.size();i++){
				for (int j=0;j<infoname.length;j++){
					List temp = (List) list.get(infoname[j]);
					sheet.addCell(new Label(j,i+1, temp.get(i).toString()));
				}
//				sheet.addCell(new Label(0,i+1,((List<String>) list.get(info[0])).get(i)));
//				sheet.addCell(new Label(1,i+1,((List<String>) list.get(info[1])).get(i)));
//				sheet.addCell(new Label(2,i+1,((List<String>) list.get(info[2])).get(i)));
			}
			// 写入数据并关闭文件
			book.write();
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			if(book!=null){
				try {
					book.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException, InvalidFormatException {
		ExcelUtils ex = new ExcelUtils();
//		ex.readexcel("input");
//		List<String> content = ex.importCsv("input/data.csv");
		System.out.println(1);
	}
}