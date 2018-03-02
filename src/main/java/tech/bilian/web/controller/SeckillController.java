package tech.bilian.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.bilian.web.dto.Exposer;
import tech.bilian.web.dto.SeckillExecution;
import tech.bilian.web.dto.SeckillResult;
import tech.bilian.web.enums.SeckillStateEnum;
import tech.bilian.web.exception.RepeatKillException;
import tech.bilian.web.exception.SeckillCloseException;
import tech.bilian.web.exception.SeckillExcepiton;
import tech.bilian.web.model.Seckill;
import tech.bilian.web.service.SeckillService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/seckill")
public class SeckillController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model)
    {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
        //list.jsp + model = modelAndView

    }


    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long  seckillId, Model model)
    {
        if(seckillId == null)
        {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getBySeckillId(seckillId);
        if(seckill == null)
        {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId)
    {
        SeckillResult<Exposer> result;
        try{
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);

        }
        catch (Exception e){
            logger.error(e.getMessage());
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
    return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
        method = RequestMethod.POST,
        produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long phone)
    {
        if(phone == null)
        {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        try{
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
        //允许的异常
        catch (RepeatKillException e1){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
        catch (SeckillCloseException e2){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            //TODO
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }


    }


    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time()
    {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

    @RequestMapping("/zhang")
    public String a()
    {
        System.out.println("zzzz");
        return "hello";
    }
}
