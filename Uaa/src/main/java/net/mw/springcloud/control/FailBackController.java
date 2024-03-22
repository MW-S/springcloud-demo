package net.mw.springcloud.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FailBackController {
    private Logger log= LogManager.getLogger(getClass());
    @RequestMapping("/error/fallback")
    public  Map<String, String> failBack(){
        Map<String, String> res = new HashMap();
        res.put("code", "500");
        res.put("data", "service not available");
        return res;
    }
}
