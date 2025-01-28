import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {ComboBox, ComboBoxSelectedItemChangedEvent, Grid, GridColumn} from '@vaadin/react-components';
import React, {useEffect, useState} from "react";
import {CustomerService} from "Frontend/generated/endpoints";
import Customer from "Frontend/generated/com/example/application/services/CustomerService/Customer";
import Order from "Frontend/generated/com/example/application/services/CustomerService/Order";

export const config: ViewConfig = {menu: {order: 1, icon: 'line-awesome/svg/react.svg'}, title: 'Hilla-View'};

export default function HillaView(){

    const [customers, setCustomers] = useState<Customer[]>([]);
    const [orders, setOrders] = useState<Order[]>([]);

    useEffect(() => {
        CustomerService.getCustomers().then(setCustomers);
    }, []);

    function selectedUserChanged(e: ComboBoxSelectedItemChangedEvent<Customer>) {
        const orders = e.detail.value ? e.detail.value.orders : [];
        setOrders(orders);
    }

    return (
        <div className="flex flex-col items-start gap-l p-m">
            <h1>Hilla microservice example</h1>

            <ComboBox label="Select user to view orders"
                      items={customers}
                      itemLabelPath="user.name"
                      onSelectedItemChanged={selectedUserChanged}/>

            <Grid items={orders}>
                <GridColumn path="product"/>
                <GridColumn path="price"/>
            </Grid>

        </div>
    );
}
