class Engine:
    variable_stack = []

    def __init__(self):
        pass

    def execute(self, code):
        for statement in code:
            self.execute_statement(statement)

    def execute_statement(self, statement):
        if "op" not in statement:
            return statement
        match statement["op"]:
            case "set_variable":
                return self.execute_set_variable(statement)
            case "get_variable":
                return self.execute_get_variable(statement)
            case "from_list_get_item":
                return self.execute_from_list_get_item(statement)
            case "print":
                print(self.execute_statement(statement["pointer"])["value"])

    def execute_variable(self, statement):
        return statement["value"]

    def execute_from_list_get_item(self, statement):
        pointer = statement["pointer"]
        variable = self.execute_statement(pointer)
        return variable["value"][statement["index"]]

    def execute_set_variable(self, statement):
        self.variable_stack.append({
            "name": statement["name"],
            "pointer": statement["pointer"]
        })

    def execute_get_variable(self, statement):
        for variable in self.variable_stack:
            if variable["name"] == statement["name"]:
                return variable["pointer"]

        return None

