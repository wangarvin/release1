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

public class AdcodeMain {
    
    public static void main(String[] args) throws IOException {
        FileOutputStream output = new FileOutputStream(new File("/Users/martin/project/micar/codebase/detaildesign/database/data/area.sql"));

        InputStream input = new FileInputStream(new File("/Users/martin/project/micar/codebase-history/database/adcode.txt"));
        List<String> lines = IOUtils.readLines(input);
        Map<String, Integer> map = new HashMap<String, Integer>();
        Stack<String> stack = new Stack<String>();
        int i = 1000;
        for (String line : lines) {
//            if (i > 200) {
//                break;
//            }
            
            String[] arr = line.split("\\s+");
            String adcode = arr[0];
            String name = arr[1];
            if (name.equals("市辖区") || name.equals("县") || name.equals("自治区直辖县级行政区划") || name.equals("省直辖县级行政区划")) {
                continue;
            }
            
            ++i;
            
            if (adcode.endsWith("0000")) {
                stack.clear();
            }
            
            map.put(adcode, i);
            
            
            String part1 = adcode.substring(0, 2);
            String part2 = adcode.substring(2, 4);
            String part3 = adcode.substring(4, 6);

            String parent = null;
            if (part3.equals("00")) {
                String peek = stack.empty() ? null : stack.peek();
                if (peek != null && !adcode.startsWith(peek)) {
                    stack.pop();
                }
                
                parent = stack.empty() ? null : stack.peek();
                if (part2.equals("00")) {
                    stack.push(part1);
                } else {
                    stack.push(part1 + part2);
                }
            }
            
            if (parent == null) {
                parent = stack.empty() ? null : stack.peek();    
            }
            
            for (int n = parent.length(); n < 6; n++) {
                parent += "0";
            }
            
//            String format = String.format("%10s%10s%10s%10s%20s", adcode, parent, map.get(adcode), map.get(parent), name);
//            System.out.println(format);
            
            Integer parentId = parent.equals(adcode) ? 0 : map.get(parent);
            String sql = String.format("INSERT INTO Area(areaId, parentId, adcode, name, status) VALUES(%s,%s, '%s', '%s', 1);\n", map.get(adcode), parentId, adcode, name);
            System.out.println(sql);
            IOUtils.write(sql, output);
            
        }
        
        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(input);
        
    }

}
