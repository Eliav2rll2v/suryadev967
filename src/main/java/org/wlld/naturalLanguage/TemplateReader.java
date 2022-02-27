package org.wlld.naturalLanguage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class TemplateReader {//模板读取类
    private Map<Integer, List<String>> model = new HashMap<>();//训练模板
    private String charsetName;

    public void read(String url, String charsetName) throws Exception {
        byte sys;
        if(System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")){
            sys = IOConst.WIN;
        }else{
            sys = IOConst.NOT_WIN;
        }
        this.charsetName = charsetName;
        File file = new File(url);
        InputStream is = new FileInputStream(file);
        int i;
        LinkedList<Byte> span = new LinkedList<>();
        int hang = 0;
        int again = 0;
        int upNub = 0;
        boolean isSymbol = false;//是否遇到分隔符
        while ((i = is.read()) > -1) {
            if (i == IOConst.TYPE_Symbol) {//遇到分隔符号
                isSymbol = true;
            } else {
                if (i == IOConst.STOP_END || i == IOConst.STOP_NEXT) {
                    isSymbol = false;
                    again = again << 1 | 1;
                    if (again == 1) {//第一次进入
                        List<String> lr = model.get(upNub);
                        //addEnd(span);
                        if (lr != null) {
                            lr.add(LinkToString(span));
                        } else {
                            List<String> lis = new ArrayList<>();
                            lis.add(LinkToString(span));
                            model.put(upNub, lis);
                        }
                        upNub = 0;
                        hang++;
                        if (sys != IOConst.WIN) {
                            again = 0;
                        }
                    } else {
                        again = 0;
                    }
                } else {
                    if (isSymbol) {
                        int type = i;
                        if (type >= 48 && type <= 57) {
                            type = type - 48;
                            if (upNub == 0) {
                                upNub = type;
                            } else {
                                upNub = upNub * 10 + type;
                            }
                        }
                    } else {
                        span.add((byte) i);
                    }
                }
            }
        }
        word();
    }

    public void word() throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.start(model);
    }

    public String LinkToString(LinkedList<Byte> mod) throws UnsupportedEncodingException {
        int b = mod.size();
        byte[] be = new byte[b];
        for (int i = 0; i < b; i++) {
            be[i] = mod.poll();
        }
        return new String(be, charsetName);
    }
}
