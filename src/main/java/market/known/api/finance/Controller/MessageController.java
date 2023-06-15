package market.known.api.finance.Controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import market.known.api.finance.Emun.ARShowType;
import market.known.api.finance.Entity.ActionResult;
import market.known.api.finance.Exception.FinanceSDKException;
import market.known.api.finance.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/messages/{seq}")
    public ActionResult<?> getMessages(@RequestAttribute String body, @PathVariable Integer seq, @RequestParam(defaultValue = "50") String limit) {

        JSONObject object = JSONUtil.parseObj(body);

        try {
            messageService.init(
                    object.get("corpId", String.class),
                    object.get("financeSecret", String.class),
                    object.get("financePrivateKey", String.class)
            );

            List<JSONObject> messagesList = messageService.getMessages(seq, Integer.parseInt(limit));

            return ActionResult.defaultOk(messagesList);

        } catch (Exception e) {
            return ActionResult.defaultFailed("5000", e.getMessage(), ARShowType.MESSAGE_ERROR);
        }
    }

    @PostMapping("/media/{id}")
    public ActionResult<?> getMedia(@RequestAttribute String body, @PathVariable String id) {

        JSONObject object = JSONUtil.parseObj(body);

        try {
            messageService.init(
                    object.get("corpId", String.class),
                    object.get("financeSecret", String.class),
                    object.get("financePrivateKey", String.class)
            );
            String filename = messageService.getMediaData(id);
            return ActionResult.defaultOk(filename);

        } catch (Exception e) {
            return ActionResult.defaultFailed("5000", e.getMessage(), ARShowType.MESSAGE_ERROR);
        }
    }

}
