package com.example.application.views.flow;

import com.example.application.services.CustomerService;
import com.example.application.services.CustomerService.*;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Objects;
import java.util.stream.Collectors;

@PageTitle("Flow-View")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.VAADIN)
public class FlowView extends VerticalLayout {

    private final Grid<CustomerService.Order> grid;

    private final CustomerService customerService;

    public FlowView(CustomerService customerService) {
        this.customerService = customerService;
        add(new H1("Flow microservice example"));

        ComboBox<User> userDetailComboBox = new ComboBox<>("Select user to view orders");
        userDetailComboBox.addValueChangeListener(this::selectedUserChanged);
        userDetailComboBox.setItems(customerService.getCustomers().stream().map(Customer::user).collect(Collectors.toSet()));
        userDetailComboBox.setItemLabelGenerator(User::name);
        add(userDetailComboBox);

        grid = new Grid<>(CustomerService.Order.class);
        grid.setColumns("product", "price");
        add(grid);
    }

    private void selectedUserChanged(AbstractField.ComponentValueChangeEvent<ComboBox<User>, User> event) {
        customerService.getCustomers().stream()
                .filter(customer -> Objects.equals(customer.user(), event.getValue()))
                .map(Customer::orders)
                .findAny()
                .ifPresent(grid::setItems);
    }
}
