package com.comando.delta.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.comando.delta.exception.MyException;
import com.comando.delta.interfaces.IStatistic;
import com.comando.delta.service.AutomobileService;
import com.comando.delta.service.IndividualService;
import com.comando.delta.service.OrderService;


@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private AutomobileService automobileService;

    @GetMapping("/list")
    public String orderList(ModelMap modelMap) {
        modelMap.addAttribute("orderList", orderService.getActiveOrderList());
        return "list_order.html";
    }

    @GetMapping("/register")
    public String createOrder(ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("individualList", individualService.getIndividualListByDependency("DGSCN"));
            modelMap.addAttribute("automobileList", automobileService.getAutomobileList());
        } catch (MyException e) {
            modelMap.addAttribute("individualList", individualService.getIndividualListByDependency("DGSCN"));
            modelMap.addAttribute("automobileList", automobileService.getAutomobileList());
            modelMap.addAttribute("error", e.getMessage());
        }
        return "create_order.html";
    }

    @PostMapping("/registered")
    public String createdOrder(
    @RequestParam String orderNumber,
    @RequestParam String place,
    @RequestParam String requestType,    
    @RequestParam List<String> servicesType,
    @RequestParam Set<Long> individuals,
    @RequestParam Set<Long> automobiles,
    ModelMap modelMap) throws MyException{
        try {
            orderService.createOrderService(orderNumber, place, requestType, servicesType, individuals, automobiles);
            return "redirect:/order/list";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            return "create_order.html";
        }
    }    

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, ModelMap modelMap) throws Exception{
        modelMap.addAttribute("individualList", individualService.getIndividualListByDependency("DGSCN"));
        modelMap.addAttribute("automobileList", automobileService.getAutomobileList());
        modelMap.addAttribute("order", orderService.getOneOrder(id));
        return "edit_order.html";
    }

    @PostMapping("/edit/{id}")
    public String editOrder(
    @RequestParam Long id,
    @RequestParam String orderNumber,
    @RequestParam String place,
    @RequestParam String requestType,    
    @RequestParam List<String> servicesType,
    @RequestParam Set<Long> individuals,
    @RequestParam Set<Long> automobiles,
    ModelMap modelMap) throws Exception{
        try {
            orderService.editOrderService(id, orderNumber, place, requestType, servicesType, individuals, automobiles);
            return "redirect:/order/list";
        } catch (Exception e) {
            modelMap.addAttribute("order", orderService.getOneOrder(id));
            modelMap.addAttribute("error", e.getMessage());
            return "edit_order.html";
        }
    }
    
    @GetMapping("/search")
    public String searchOrder(String number, ModelMap modelMap) {
        modelMap.addAttribute("orderList", orderService.getOneOrderByNumber(number));
        return "list_order.html";
    }

    @GetMapping("/load")
    public String statusOrder(String status, ModelMap modelMap){
        modelMap.addAttribute("orderList", orderService.getOrderListByStatus(status));
        return "list_order.html";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("order", orderService.getOneOrder(id));
        return "";
    }

    @PostMapping("/finish/{id}")
    public String finishOrder(@PathVariable Long id, ModelMap modelMap){
        orderService.finishOrderService(id);
        modelMap.addAttribute("orderList", orderService.getInactiveOrderList());

        return "list_order.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/reset")
    public String resetOrder(){
        orderService.deleteAll();
        return "redirect:/order/list";
    }

    @GetMapping("/statistics")
    public String viewStatistics(ModelMap modelMap){
        List<IStatistic> stats = orderService.getStatistics();
        modelMap.addAttribute("stats", stats);
        return "view_statistics.html";
    }
    
}
