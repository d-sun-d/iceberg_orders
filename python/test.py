import os
from trade import Stock

class MyStream(object):
    def __init__(self):
        self.lines = []
        self.position = 0

    def write(self, line):
        #print line.strip()
        self.lines.append(line)

    def __iter__(self):
        while len(self.lines) > self.position:
            yield self.next()

    def next(self):
        if len(self.lines) > self.position:
            self.position += 1
            return self.lines[self.position-1]
        raise StopIteration


if __name__ == "__main__":
    test_case = "1"
    test_input = open(os.path.join(os.path.abspath(__file__),"..","..","tests","input_tests", test_case+".input"))
    canonical_output = open(os.path.join(os.path.abspath(__file__),"..","..","tests","input_tests", test_case+".output"))
    work_output = MyStream()

    stock = Stock(output_stream=work_output)
    for line in test_input:
        stock.process_row(line)
        stock.print_self()

    while True:
        try:
            work_line = work_output.next()
        except StopIteration:
            for line in canonical_output:
                print "c"+line.strip()
            break
        try:
            canonical_line = canonical_output.next()
        except:
            for line in work_output:
                print "w"+line.strip()
            break

        if work_line == canonical_line:
            print work_line.strip()
        else:
            print "c"+canonical_line.strip()
            print "w"+work_line.strip()