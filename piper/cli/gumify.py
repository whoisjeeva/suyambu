import json

from engine import Engine


class Gumify:
    def __init__(self):
        self.engine = Engine()

    def run(self, input_file):
        with open(input_file, 'r') as f:
            code = json.load(f)

        self.engine.execute(code)
        print(self.engine.variable_stack)


if __name__ == "__main__":
    gumify = Gumify()
    gumify.run("script.json")
