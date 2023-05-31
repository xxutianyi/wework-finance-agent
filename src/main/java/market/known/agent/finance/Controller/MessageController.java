package market.known.agent.finance.Controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import market.known.agent.finance.Emun.ARShowType;
import market.known.agent.finance.Entity.ActionResult;
import market.known.agent.finance.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/init")
    public ActionResult<?> setCredential(@RequestAttribute String body) {

        JSONObject object = JSONUtil.parseObj(body);

        try {
            messageService.setCredential(
                    object.get("corpId", String.class),
                    object.get("financeSecret", String.class),
                    object.get("financePrivateKey", String.class)
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return ActionResult.defaultFailed("4000", e.getMessage(), ARShowType.MESSAGE_ERROR);
        }

        return ActionResult.defaultOk();
    }

    @DeleteMapping("/{corpID}/delete")
    public ActionResult<?> deleteCredential(@PathVariable String corpID) {
        messageService.deleteCredential(corpID);
        return ActionResult.defaultOk();
    }


    @GetMapping("/{corpID}/messages")
    public ActionResult<List<JSONObject>> getMessages(@PathVariable String corpID, @RequestParam(defaultValue = "0") Integer from) {

        try {
            messageService.init(corpID);

            List<JSONObject> messagesList = messageService.getMessages(from, 100, null, null, 5000);

            return ActionResult.defaultOk(messagesList);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/{corpID}/media/{id}")
    public ActionResult<?> getMedia(@PathVariable String corpID, @PathVariable String id) {

        try {
            messageService.init(corpID);
            String filename = messageService.getMediaData(id);
            return ActionResult.defaultOk(filename);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
