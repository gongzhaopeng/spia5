package com.atzu68.spia5.tacocloud.controller;

import com.atzu68.spia5.tacocloud.configuration.DiscountCodeProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/discounts")
public class DiscountController {

    private DiscountCodeProps discountCodeProps;

    @Autowired
    public DiscountController(
            DiscountCodeProps discountCodeProps) {

        this.discountCodeProps = discountCodeProps;
    }

    @GetMapping
    public String displayDiscountCodes(Model model) {

        var codes = discountCodeProps.getCodes();
        model.addAttribute("codes", codes);

        return "discountList";
    }
}
