package com.opc.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opc.pojo.OpcModel;
import com.opc.pojo.ResultBean;
import com.opc.service.OpcConnectTool;

import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.property.PropertyLoader;
import javafish.clients.opc.variant.Variant;

@Controller
public class OpcService {
	public static final Logger LOGGER = LoggerFactory.getLogger(OpcService.class);
	
	@Autowired
	private OpcConnectTool opcConnectTool;
	
	@GetMapping("/testopc")
	public String getName() {
		JOpc.coInitialize();

        JOpc jopc = new JOpc("localhost", "Kepware.KEPServerEX.V5", "JOPC1");
        /**
         * param1 : item Name
         * param2 : 是否激活查询
         * param3 : 路径
         */
        OpcItem item1 = new OpcItem("Device1.Tag1", true, "Channel1");
        OpcItem item2 = new OpcItem("Channel1.Device1.Tag2", true, "");

        /**
         * 实例化分组，添加item
         * param1 : 组名
         * param2 : 是否激活查询
         * param3 : 查询频率（异步使用）
         * param4 : 不工作占比
         */
        OpcGroup group = new OpcGroup("group1", true, OpcModel.UPDATE_RATE, 0.0f);
        group.addItem(item1);
        group.addItem(item2);

        jopc.addGroup(group);

        try {
            jopc.connect();
            LOGGER.info("JOPC client is connected...");

            jopc.registerGroups();
            LOGGER.info("OPCGroup are registered...");

            jopc.registerItem(group,item1);
            jopc.registerItem(group,item2);

            /*synchronized(test) {
                test.wait(50);
            }*/
        }catch (Exception e2) {
            e2.printStackTrace();
        }

        // Synchronous reading of item
        int cycles = 2;
        int acycle = 0;
        while (acycle++ < cycles) {
            try {
                Thread.sleep(1000);

                OpcItem responseItem = jopc.synchReadItem(group, item1);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = responseItem.getTimeStamp();
                Date date = cal.getTime();
                LOGGER.info("1 ==>"+ responseItem.getItemName() + " : " + responseItem.getValue()
                        + " : " + responseItem.getAccessPath() + " : " + responseItem.getDataType() + " : " + Variant.getVariantName(responseItem.getDataType()) + " : "
                        + sdf.format(date));

                /*OpcItem responseItem2 = jopc.synchReadItem(group, item2);
                cal = responseItem.getTimeStamp();
                date = cal.getTime();
                LOGGER.info("2 ==>"+ responseItem2.getItemName() + " : " + responseItem2.getValue()
                        + " : " + responseItem2.getAccessPath() + " : " + responseItem2.getDataType() + " : " + Variant.getVariantName(responseItem2.getDataType())
                        + sdf.format(date));*/
                //LOGGER.info(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());

                //写数据
                //item1.setValue(new Variant(0));
                //jopc.synchWriteItem(group,item1);

                //item2.setValue(new Variant(acycle));
                //jopc.synchWriteItem(group,item2);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        //释放资源
        /*try {
            jopc.unregisterItem(group, item1);
            jopc.unregisterItem(group, item2);
            jopc.unregisterGroup(group);
        } catch (UnableRemoveItemException e) {
            e.printStackTrace();
        } catch (UnableRemoveGroupException e) {
            e.printStackTrace();
        }*/

        JOpc.coUninitialize();
        
		return "index";
	}
	
	@GetMapping(value="/index")
	public String index(Model model) {
		model.addAttribute("javalib", "java lib : "+System.getProperty("java.library.path"));
		model.addAttribute("result","");
		
		return "index";
	}
	
	@PostMapping(value="/queryparam")
	public String queryparam(OpcModel server,Model model) {
		long startTime = System.currentTimeMillis();
		model.addAttribute("javalib", "java lib : "+System.getProperty("java.library.path"));

		ResultBean result = opcConnectTool.queryOpcServer(server);
		model.addAttribute("result","【"+result.isSuccess()+"】，结果：" + result.getResult());
		
		LOGGER.info("共耗时：" + (System.currentTimeMillis() - startTime));
		return "index";
	}
	
	@PostMapping(value="/editparam")
	public String editparam(OpcModel server, String editValue, Model model) {
		model.addAttribute("javalib", "java lib : "+System.getProperty("java.library.path"));
		
		ResultBean result = opcConnectTool.editOpcParam(server, editValue);
		model.addAttribute("result","【"+result.isSuccess()+"】，结果：" + result.getResult());
		
		return "index";
	}
}
