package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

public class CategoryMain {
    
    public static void main(String[] args) throws IOException {
        FileOutputStream output = new FileOutputStream(new File("/Users/martin/project/micar/codebase/detaildesign/database/data/category.sql"));

        InputStream input = new FileInputStream(new File("/Users/martin/Documents/workspace/micar/mybatis-gen/data/category.csv"));
        List<String> lines = IOUtils.readLines(input);
        Map<String, Integer> map = new HashMap<String, Integer>();
        Stack<String> stack = new Stack<String>();
        
        
        int category1 = 0;
        int category2 = 0;
        int category3 = 0;
        int attribute1Id = 0;
        int attribute2Id = 0;
        int opt = 0;
        int i = 1;
        
        for (String line : lines) {

            // 汽车用品,维修保养,润滑油/机油,类别,汽机油,
            String[] items = line.split(",");
            if (items.length < 5) {
                continue;
            }
            if (!items[0].trim().isEmpty()) {
                category1 = i++;
                
                String sql = String.format("INSERT INTO Category1(category1Id, category1Name, displayOrder, status) VALUES(%s,'%s', 0, 1);\n", category1, items[0]);
                out(output, sql);
            }
            if (!items[1].trim().isEmpty()) {
                category2 = i++;
                
                String sql = String.format("INSERT INTO Category2(category2Id, category1Id, category2Name, displayOrder, status) VALUES(%s,%s,'%s', 0, 1);\n", category2, category1, items[1]);
                out(output, sql);
            }
            if (!items[2].trim().isEmpty()) {
                category3 = i++;
                
                String sql = String.format("INSERT INTO Category3(category3Id, category2Id, category3Name, displayOrder, status) VALUES(%s,%s,'%s', 0, 1);\n", category3, category2, items[2]);
                out(output, sql);

                
                // 新一级属性
                attribute1Id = i++;

                sql = String.format("INSERT INTO CategoryAttribute1(attribute1Id, category3Id, attribute1Name, displayOrder, status) VALUES(%s,%s,'%s', 0, 1);\n", attribute1Id, category3, "规格参数");
                out(output, sql);

            }
            if (!items[3].trim().isEmpty()) {
                attribute2Id = i++;
                
                String sql = String.format("INSERT INTO CategoryAttribute2(attribute2Id, attribute1Id, attribute2Name, attribute2Type, showType, displayOrder, status) VALUES(%s,%s,'%s', 1, 1, 0, 1);\n", attribute2Id, attribute1Id, items[3]);
                out(output, sql);
                
            }
            if (!items[4].trim().isEmpty()) {
                opt = i++;

                String sql = String.format("INSERT INTO CategoryAttribute2Option(attribute2OptionId, attribute2Id, optionName, displayOrder, status) VALUES(%s,%s,'%s', 0, 1);\n", opt, attribute2Id, items[4]);
                out(output, sql);
            }
        }
        
        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(input);
        
    }

    private static void out(FileOutputStream output, String sql) throws IOException {
        System.out.println(sql);
        IOUtils.write(sql, output);
    }

}

