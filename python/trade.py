import sys
import os


class Order(object):
    def __init__(self, time, buy_sell, order_id, price, size, hidden_size=None):
        self.buy_sell = buy_sell
        self.order_id = int(order_id)
        self.price  = int(price)
        self.size =int(size)
        self.hidden_size = hidden_size if hidden_size is not None else size
        # ordinary order is same as hidden with peak size equal quantity of order
        self.birth_time  = time
        self.update_time = self.birth_time

    @staticmethod
    def order_from_row(time, row):
        parts = row.split(",")
        if len(parts) == 4:
            buy_sell, order_id, price, size = parts
            return Order(time, buy_sell, order_id, price, size)
        else:
            buy_sell, order_id, price, hidden_size, size  = parts
            return  Order(time, buy_sell, order_id, price, size, hidden_size)

    def is_empty(self):
        if self.hidden_size == 0:
            return True
        return False


class ListBasedOrderQueue(object):
    def __init__(self, buy_sell):
        self.buy_sell = buy_sell
        self._list = []

    def _cmp(self, order1, order2):
        order = (order1.price, order1.update_time, order1.birth_time) > \
            (order2.price, order2.update_time, order2.birth_time)
        return 1 if order else -1

    def add(self, order):
        self._list.append(order)
        self._list.sort(cmp=self._cmp)

    def __iter__(self):
        for rec in self._list:
            yield rec


class Stock(object):
    def __init__(self, output_stream=sys.stdout):
        self.GLOBAL_TIME = 1
        self.output_stream = output_stream
        # TODO write more efficient realization of OrderQueue
        self.buy_orders = ListBasedOrderQueue("B")
        self.sell_orders = ListBasedOrderQueue("S")

    def _get_time(self):
        self.GLOBAL_TIME += 1
        return self.GLOBAL_TIME

    def process_row(self, row):
        order = Order.order_from_row(self._get_time(), row)
        self.process_order(order)

    def match_order(self, order):
        pass

    def process_order(self, order):
        self.match_order(order)
        if not order.is_empty():
            if order.buy_sell == "B":
                self.buy_orders.add(order)
            else:
                self.sell_orders.add(order)

    def print_self(self):
        self.output_stream.write("+-----------------------------------------------------------------+\n")
        self.output_stream.write("| BUY                            | SELL                           |\n")
        self.output_stream.write("| Id       | Volume      | Price | Price | Volume      | Id       |\n")
        self.output_stream.write("+----------+-------------+-------+-------+-------------+----------+\n")

        def format_order(order, buy_sell):
            if order is None:
                id_str = " "*10
                volume_str = " "*13
                price_str = " "*7
            else:
                id_str = "{:>10}".format(order.order_id)
                volume_str = "{:>13,}".format(order.size)
                price_str = "{:>7,}".format(order.price)
            if buy_sell == "B":
                return "|".join([id_str, volume_str, price_str])
            else:
                return "|".join([price_str, volume_str, id_str])

        buy_orders = list(self.buy_orders)
        sell_orders = list(self.sell_orders)
        while len(buy_orders) < len(sell_orders):
            buy_orders.append(None)
        while len(sell_orders) < len(buy_orders):
            sell_orders.append(None)
        for buy, sell in zip(buy_orders, sell_orders):
            self.output_stream.write("|" + format_order(buy, "B") + "|" + format_order(sell, "S") + "|\n")

        self.output_stream.write("+-----------------------------------------------------------------+\n")







