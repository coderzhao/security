package cn.anytec.security.controller;

import cn.anytec.security.common.ServerResponse;
import cn.anytec.security.component.ipcamera.ipcService.IPCOperations;
import cn.anytec.security.core.annotion.OperLog;
import cn.anytec.security.model.TbCamera;
import cn.anytec.security.service.CameraService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/camera")
public class CameraController {

    @Autowired
    private CameraService cameraService;
    @Autowired
    private IPCOperations ipcOperations;


    @OperLog(value = "添加设备", key="id,name")
    @PostMapping("/camera/add")
    public ServerResponse add(TbCamera camera){
        return cameraService.add(camera);
    }

    @OperLog(value = "删除设备", key = "cameraIds")
    @RequestMapping("/camera/delete")
    @ResponseBody
    public ServerResponse delete(@RequestParam(value = "cameraIds") String cameraIds){
        return cameraService.delete(cameraIds);
    }

//    @OperLog(value = "查询设备列表", key = "type")
    @RequestMapping("/camera/list")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                               @RequestParam(value = "name",required = false)String name,
                               @RequestParam(value = "groupId",required = false)Integer groupId,
                               @RequestParam(value = "type",required = false)String type,
                               @RequestParam(value = "serverLabel",required = false)String serverLabel,
                               @RequestParam(value = "status",required = false)Integer status,
                               @RequestParam(value = "cameraSdkId",required = false)String cameraSdkId){

        List<TbCamera> cameraList = cameraService.list(pageNum,pageSize,name,groupId,type,serverLabel,status,cameraSdkId);
        PageInfo pageResult = new PageInfo(cameraList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @OperLog(value = "修改设备信息", key="id,name")
    @RequestMapping("/camera/update")
    public ServerResponse update(TbCamera camera){
        cameraService.getCameraInfo(camera.getId());
        return cameraService.update(camera);
    }

    @GetMapping("/cameras")
    public String getCameras() {
        return cameraService.cameras();
    }


    @GetMapping("/camera/connect")
    public String connectCamera(Integer id){
        return cameraService.connect(id);
    }

    @GetMapping("/camera/deleteConnect")
    public ServerResponse deleteCamera(Integer id){
        return cameraService.deleteCameraConnect(id);
    }

    @RequestMapping("/camera/getServerLabel")
    public ServerResponse getServerLabel(){
        return cameraService.getServerLabel();
    }

    @RequestMapping("/getCaptureCameras")
    public Map<String, String> getCaptureCameras(){
        return ipcOperations.getCaptureCameras();
    }

    @RequestMapping("/activeCaptureCamera")
    public boolean activeCaptureCamera(@RequestParam(value = "macAddress")String macAddress, @RequestParam(value = "ipcAddress")String ipcAddress){
        return ipcOperations.activeCaptureCamera(macAddress,ipcAddress);
    }

    @RequestMapping("/invalidCaptureCamera")
    public boolean invalidCaptureCamera(@RequestParam(value = "macAddress")String macAddress, @RequestParam(value = "ipcAddress")String ipcAddress){
        return ipcOperations.invalidCaptureCamera(macAddress,ipcAddress);
    }

}
