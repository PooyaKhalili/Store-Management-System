package com.storesystem.controller;

import com.ibm.icu.impl.UResource;
import com.storesystem.service.ReportService;
import com.storesystem.util.TableUtil;
import com.storesystem.view.ReportPanel;

import java.util.List;

public class ReportController {
    private ReportPanel view;
    private ReportService reportService;

    public ReportController(ReportPanel view){
        this.view = view;
        this.reportService = new ReportService();
        initController();
        loadReports();
    }

    public void initController(){
        view.getRefreshButton().addActionListener(e ->{
            loadReports();
        });
    }

    public void loadReports(){
        List<Object[]> topProducts = reportService.getTopsellingProducts();
        TableUtil.refreshTable(view.getTopTable(),topProducts);

        List<Object[]> customerSummery = reportService.getCustomerPurchaseSummary();
        TableUtil.refreshTable(view.getBottomTable(),customerSummery);

        double totalRevenue = reportService.getTotalAmount();
        view.setTotalRevenue((int)totalRevenue);
    }

}
