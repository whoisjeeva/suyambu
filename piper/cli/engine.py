import json
import math
import random

import util


class Engine:
    variable_stack = {}
    function_stack = []

    def __init__(self):
        self.loop_flow = None
        self.current_function_name = None
        self.function_return = None
        self.function_return_value = None

    def find_function(self, name):
        for function in Engine.function_stack:
            if function["name"] == name:
                return function
        return None

    def execute(self, code):
        for statement in code:
            if self.loop_flow == "BREAK":
                break
            elif self.loop_flow == "CONTINUE":
                continue

            if self.function_return is not None:
                break
            
            self.execute_statement(statement)

    def execute_statement(self, statement):
        try:
            statement = json.loads(statement)
        except:
            pass

        if type(statement) is not dict:
            return statement

        if "cmd" not in statement:
            return statement

        match statement["cmd"]:
            # Navigator
            case "go_to_website":
                return self.execute_navigator_go_to_website(statement)

            # Logic
            case "controls_if":
                return self.execute_controls_if(statement)
            case "logic_boolean":
                return self.execute_logic_boolean(statement)
            case "logic_negate":
                return self.execute_logic_negate(statement)
            case "logic_operation":
                return self.execute_logic_operation(statement)
            case "logic_compare":
                return self.execute_logic_compare(statement)
            case "logic_ternary":
                return self.execute_logic_ternary(statement)
            case "logic_null":
                return self.execute_logic_null(statement)

            # Loops
            case "controls_repeat_ext":
                return self.execute_controls_repeat_ext(statement)
            case "controls_whileUntil":
                return self.execute_controls_whileUntil(statement)
            case "controls_for":
                return self.execute_controls_for(statement)
            case "controls_forEach":
                return self.execute_controls_forEach(statement)
            case "controls_flow_statements":
                self.loop_flow = statement["pointer"]["flow"]
                return self.loop_flow

            # Math
            case "math_number":
                return self.execute_math_number(statement)
            case "math_arithmetic":
                return self.execute_math_arithmetic(statement)
            case "math_single":
                return self.execute_math_single(statement)
            case "math_trig":
                return self.execute_math_trig(statement)
            case "math_constant":
                return self.execute_math_constant(statement)
            case "math_number_property":
                return self.execute_math_number_property(statement)
            case "math_round":
                return self.execute_math_round(statement)
            case "math_on_list":
                return self.execute_math_on_list(statement)
            case "math_modulo":
                return self.execute_math_modulo(statement)
            case "math_constrain":
                return self.execute_math_constrain(statement)
            case "math_random_int":
                return self.execute_math_random_int(statement)
            case "math_random_float":
                return self.execute_math_random_float(statement)


            # Text
            case "text":
                return self.execute_text(statement)
            case "text_join":
                return self.execute_text_join(statement)
            case "text_append":
                return self.execute_text_append(statement)
            case "text_length":
                return self.execute_text_length(statement)
            case "text_isEmpty":
                return self.execute_text_isEmpty(statement)
            case "text_indexOf":
                return self.execute_text_indexOf(statement)
            case "text_charAt":
                return self.execute_text_charAt(statement)
            case "text_getSubstring":
                return self.execute_text_getSubstring(statement)
            case "text_changeCase":
                return self.execute_text_changeCase(statement)
            case "text_trim":
                return self.execute_text_trim(statement)
            case "text_print":
                return self.execute_text_print(statement)
            case "text_prompt_ext":
                return self.execute_text_prompt_ext(statement)


            # Lists
            case "lists_create_with":
                return self.execute_lists_create_with(statement)
            case "lists_repeat":
                return self.execute_lists_repeat(statement)
            case "lists_length":
                return self.execute_lists_length(statement)
            case "lists_isEmpty":
                return self.execute_lists_isEmpty(statement)
            case "lists_indexOf":
                return self.execute_lists_indexOf(statement)
            case "lists_getIndex":
                return self.execute_lists_getIndex(statement)
            case "lists_setIndex":
                return self.execute_lists_setIndex(statement)
            case "lists_getSublist":
                return self.execute_lists_getSublist(statement)
            case "lists_split":
                return self.execute_lists_split(statement)
            case "lists_sort":
                return self.execute_lists_sort(statement)


            # Variables
            case "variables_set":
                return self.execute_variables_set(statement)
            case "variables_get":
                return self.execute_variables_get(statement)
            case "math_change":
                return self.execute_math_change(statement)


            # Functions
            case "procedures_defnoreturn":
                return self.execute_procedures_defnoreturn(statement)
            case "procedures_callnoreturn":
                return self.execute_procedures_callnoreturn(statement)
            case "procedures_ifreturn":
                return self.execute_procedures_ifreturn(statement)
            case "procedures_defreturn":
                return self.execute_procedures_defreturn(statement)
            case "procedures_callreturn":
                return self.execute_procedures_callreturn(statement)


    # Navigator

    def execute_navigator_go_to_website(self, statement):
        print("execute_navigator_go_to_website")



    # Logic

    def execute_controls_if(self, statement):
        print("execute_controls_if")
        pointer = statement["pointer"]
        for block in pointer:
            if "else" in block:
                self.execute(json.loads(block["else"]))
            else:
                cond = self.execute_statement(block["cond"])
                if cond:
                    if block["do"].strip() != "":
                        self.execute(json.loads(block["do"]))
                    break

    def execute_logic_boolean(self, statement):
        print("execute_logic_boolean")
        pointer = statement["pointer"]
        return pointer

    def execute_logic_negate(self, statement):
        print("execute_logic_negate")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer)
        if value == "False":
            return True
        elif value == "True":
            return False
        return False

    def execute_logic_operation(self, statement):
        print("execute_logic_operation")
        pointer = statement["pointer"]
        op = pointer["op"]
        value1 = self.execute_statement(pointer["value1"])
        value2 = self.execute_statement(pointer["value2"])

        if op == "&&":
            return value1 and value2
        elif op == "||":
            return value1 or value2
        return False

    def execute_logic_compare(self, statement):
        print("execute_logic_compare")
        pointer = statement["pointer"]
        op = pointer["op"]
        value1 = self.execute_statement(pointer["value1"])
        value2 = self.execute_statement(pointer["value2"])

        if op == "==":
            return value1 == value2
        elif op == "!=":
            return value1 != value2
        elif op == ">":
            return value1 > value2
        elif op == "<":
            return value1 < value2
        elif op == ">=":
            return value1 >= value2
        elif op == "<=":
            return value1 <= value2
        return False

    def execute_logic_ternary(self, statement):
        print("execute_logic_ternary")
        pointer = statement["pointer"]
        cond = self.execute_statement(pointer["cond"])
        if cond:
            return self.execute_statement(pointer["value1"])
        return self.execute_statement(pointer["value2"])

    def execute_logic_null(self, statement):
        print("execute_logic_null")
        pointer = statement["pointer"]
        return None




    # Loops

    def execute_controls_repeat_ext(self, statement):
        print("execute_controls_repeat_ext")
        self.loop_flow = None
        pointer = statement["pointer"]
        times = self.execute_statement(pointer["times"])
        loop_block = pointer["loopBlock"]
        if loop_block.strip() != "":
            for i in range(0, times):
                self.execute(json.loads(loop_block))
                if self.loop_flow == "BREAK":
                    self.loop_flow = None
                    break
                elif self.loop_flow == "CONTINUE":
                    self.loop_flow = None
                    continue

                if self.function_return is not None:
                    break

    def execute_controls_whileUntil(self, statement):
        print("execute_controls_whileUntil")
        self.loop_flow = None
        pointer = statement["pointer"]
        loop_block = pointer["loopBlock"]
        mode = pointer["mode"]
        
        while True:
            cond = self.execute_statement(pointer["cond"])
            if mode == "UNTIL":
                if cond: break
            else:
                if not cond: break

            print("Executing while/until loop")
            self.execute(json.loads(loop_block))
            if self.loop_flow == "BREAK":
                self.loop_flow = None
                break
            elif self.loop_flow == "CONTINUE":
                self.loop_flow = None
                continue

            if self.function_return is not None:
                break

    def execute_controls_for(self, statement):
        print("execute_controls_for")
        self.loop_flow = None
        pointer = statement["pointer"]
        start = int(self.execute_statement(pointer["from"]))
        end = int(self.execute_statement(pointer["to"]))
        step = int(self.execute_statement(pointer["step"]))
        variable = pointer["variable"]
        loop_block = pointer["loopBlock"]
        for i in range(start, end+1, step):

            is_set = False
            if self.current_function_name is not None:
                f = self.find_function(self.current_function_name)
                if variable in f["stack"]:
                    f["stack"][variable] = i
                    is_set = True
            if not is_set:
                self.variable_stack[variable] = i
                
            self.execute(json.loads(loop_block))
            if self.loop_flow == "BREAK":
                self.loop_flow = None
                break
            elif self.loop_flow == "CONTINUE":
                self.loop_flow = None
                continue

            if self.function_return is not None:
                break

    def execute_controls_forEach(self, statement):
        print("execute_controls_forEach")
        self.loop_flow = None
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["list"])
        loop_block = pointer["loopBlock"]
        variable = pointer["variable"]
        for item in lst:
            
            is_set = False
            if self.current_function_name is not None:
                f = self.find_function(self.current_function_name)
                if variable in f["stack"]:
                    f["stack"][variable] = item
                    is_set = True
            if not is_set:
                self.variable_stack[variable] = item
            
            self.execute(json.loads(loop_block))
            if self.loop_flow == "BREAK":
                self.loop_flow = None
                break
            elif self.loop_flow == "CONTINUE":
                self.loop_flow = None
                continue

            if self.function_return is not None:
                break


    # Math

    def execute_math_number(self, statement):
        print("execute_math_number")
        pointer = statement["pointer"]
        return pointer

    def execute_math_arithmetic(self, statement):
        print("execute_math_arithmetic")
        pointer = statement["pointer"]
        op = pointer["op"]
        value1 = self.execute_statement(pointer["value1"])
        value2 = self.execute_statement(pointer["value2"])

        output = "0"
        if op == "ADD":
            output = value1 + value2
        elif op == "MINUS":
            output = value1 - value2
        elif op == "MULTIPLY":
            output = value1 * value2
        elif op == "DIVIDE":
            output = value1 / value2
        elif op == "POWER":
            output = value1 ** value2
        return output
    
    def execute_math_single(self, statement):
        print("execute_math_single")
        pointer = statement["pointer"]
        op = pointer["op"]
        value = self.execute_statement(pointer["value"])

        output = "0"
        if op == "ROOT":
            output = math.sqrt(value)
        elif op == "ABS":
            output = abs(value)
        elif op == "NEG":
            output = -value
        elif op == "LN":
            output = math.log(value)
        elif op == "LOG10":
            output = math.log10(value)
        elif op == "EXP":
            output = math.exp(value)
        elif op == "POW10":
            output = math.power(10, value)
        return output

    def execute_math_trig(self, statement):
        print("execute_math_trig")
        pointer = statement["pointer"]
        op = pointer["op"]
        value = self.execute_statement(pointer["value"])

        output = "0"
        if op == "SIN":
            output = math.sin(value / 180 * math.pi)
        elif op == "COS":
            output = math.cos(value / 180 * math.pi)
        elif op == "TAN":
            output = math.tan(value / 180 * math.pi)
        elif op == "ASIN":
            output = math.asin(value) / math.pi * 180
        elif op == "ACOS":
            output = math.acos(value) / math.pi * 180
        elif op == "ATAN":
            output = math.atan(value) / math.pi * 180
        return output

    def execute_math_constant(self, statement):
        print("execute_math_const")
        pointer = statement["pointer"]
        output = "0"

        if pointer["const"] == "PI":
            output = math.pi
        elif pointer["const"] == "E":
            output = math.e
        elif pointer["const"] == "GOLDEN_RATIO":
            output = (1 + math.sqrt(5)) / 2
        elif pointer["const"] == "SQRT2":
            output = math.sqrt(2)
        elif pointer["const"] == "SQRT1_2":
            output = math.sqrt(1/2)
        elif pointer["const"] == "INFINITY":
            output = math.inf
        return output
    
    def execute_math_number_property(self, statement):
        print("execute_math_number_property")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value1"])
        prop = pointer["property"]

        output = "false"
        if prop == "EVEN":
            output = value % 2 == 0
        elif prop == "ODD":
            output = value % 2 != 0
        elif prop == "PRIME":
            output = util.is_prime(value)
        elif prop == "WHOLE":
            output = value % 1 == 0
        elif prop == "POSITIVE":
            output = value > 0
        elif prop == "NEGATIVE":
            output = value < 0
        elif prop == "DIVISIBLE_BY":
            output = value % int(self.execute_statement(pointer["value2"])) == 0
        return output

    def execute_math_round(self, statement):
        print("execute_math_round")
        pointer = statement["pointer"]
        op = pointer["op"]
        value = self.execute_statement(pointer["value"])
        if op == "ROUND":
            return round(value)
        elif op == "ROUNDUP":
            return math.ceil(value)
        elif op == "ROUNDDOWN":
            return math.floor(value)

    def execute_math_on_list(self, statement):
        print("execute_math_on_list")
        pointer = statement["pointer"]
        op = pointer["op"]
        values = self.execute_statement(pointer["list"])

        output = 0
        if op == "SUM":
            output = sum(values)
        elif op == "MIN":
            output = min(values)
        elif op == "MAX":
            output = max(values)
        elif op == "AVERAGE":
            output = sum(values) / len(values)
        elif op == "MEDIAN":
            values.sort()
            if len(values) % 2 == 0:
                output = (values[len(values) // 2] + values[len(values) // 2 - 1]) / 2
            else:
                output = values[len(values) // 2]
        elif op == "MODE": # maximum occuring value
            values.sort()
            count = 1
            mode = values[0]
            for i in range(1, len(values)):
                if values[i] == values[i - 1]:
                    count += 1
                else:
                    count = 1
                if count > 1:
                    mode = values[i]
                    break
            output = mode
        elif op == "STD_DEV":
            mean = sum(values) / len(values)
            output = math.sqrt(sum([(x - mean) ** 2 for x in values]) / len(values))
        elif op == "RANDOM":
            output = random.choice(values)
        return output

    def execute_math_modulo(self, statement):
        print("execute_math_modulo")
        pointer = statement["pointer"]
        value1 = self.execute_statement(pointer["value1"])
        value2 = self.execute_statement(pointer["value2"])
        return value1 % value2

    def execute_math_constrain(self, statement):
        print("execute_math_constrain")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value"])
        minValue = self.execute_statement(pointer["min"])
        maxValue = self.execute_statement(pointer["max"])
        return max(min(value, maxValue), minValue)

    def execute_math_random_int(self, statement):
        print("execute_math_random_int")
        pointer = statement["pointer"]
        minValue = self.execute_statement(pointer["min"])
        maxValue = self.execute_statement(pointer["max"])
        return random.randint(minValue, maxValue)

    def execute_math_random_float(self, statement):
        print("execute_math_random_float")
        return random.uniform(0, 1)



    # Text

    def execute_text(self, statement):
        return statement["pointer"]

    def execute_text_join(self, statement):
        print("execute_text_join")
        pointer = statement["pointer"]
        values = []
        for value in pointer:
            values.append(str(self.execute_statement(value)))
        return "".join(values)

    def execute_text_append(self, statement):
        print("execute_text_append")
        pointer = statement["pointer"]
        variable_name = pointer["variable"]
        text = self.execute_statement(pointer["text"])

        is_set = False
        if self.current_function_name is not None:
            f = self.find_function(self.current_function_name)
            if variable_name in f["stack"]:
                f["stack"][variable_name] += text
                is_set = True

        if not is_set:
            if variable_name in self.variable_stack:
                self.variable_stack[variable_name] += text
            else:
                self.variable_stack[variable_name] = text

    def execute_text_length(self, statement):
        print("execute_text_length")
        pointer = statement["pointer"]
        text = self.execute_statement(pointer)
        return len(text)

    def execute_text_isEmpty(self, statement):
        print("execute_text_isEmpty")
        pointer = statement["pointer"]
        text = self.execute_statement(pointer)
        return len(text) == 0

    def execute_text_indexOf(self, statement):
        print("execute_text_indexOf")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value"])
        substring = self.execute_statement(pointer["substring"])
        indexOf = pointer["indexOf"]

        if indexOf == "FIRST":
            return value.find(substring) + 1
        elif indexOf == "LAST":
            return value.rfind(substring) + 1
        return -1

    def execute_text_charAt(self, statement):
        print("execute_text_charAt")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value"])
        at = self.execute_statement(pointer["at"])
        action = pointer["action"]

        if action == "FROM_START":
            return value[at - 1]
        elif action == "FROM_END":
            return value[len(value) - at]
        elif action == "FIRST":
            return value[0]
        elif action == "LAST":
            return value[len(value) - 1]
        elif action == "RANDOM":
            return value[random.randint(0, len(value) - 1)]
        return ""

    def execute_text_getSubstring(self, statement):
        print("execute_text_getSubstring")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value"])
        at1 = self.execute_statement(pointer["at1"])
        at2 = self.execute_statement(pointer["at2"])
        action1 = pointer["action1"]
        action2 = pointer["action2"]

        if action1 == "FROM_START":
            start = at1 - 1
        elif action1 == "FROM_END":
            start = len(value) - at1
        elif action1 == "FIRST":
            start = 0

        if action2 == "FROM_START":
            end = at2 - 1
        elif action2 == "FROM_END":
            end = len(value) - at2
        elif action2 == "LAST":
            end = len(value) - 1

        return value[start:end+1]

    def execute_text_changeCase(self, statement):
        print("execute_text_changeCase")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value"])
        action = pointer["action"]

        if action == "UPPERCASE":
            return value.upper()
        elif action == "LOWERCASE":
            return value.lower()
        elif action == "TITLECASE":
            return value.title()
        return value

    def execute_text_trim(self, statement):
        print("execute_text_trim")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer["value"])
        action = pointer["action"]

        if action == "LEFT":
            return value.lstrip()
        elif action == "RIGHT":
            return value.rstrip()
        elif action == "BOTH":
            return value.strip()
        return value

    def execute_text_print(self, statement):
        print("execute_text_print")
        pointer = statement["pointer"]
        value = self.execute_statement(pointer)
        print(value)
    
    def execute_text_prompt_ext(self, statement):
        print("execute_text_prompt_ext")
        pointer = statement["pointer"]
        input_type = pointer["type"]
        label = self.execute_statement(pointer["label"])

        if input_type == "NUMBER":
            try:
                s = input(label)
                if s.find(".") != -1:
                    return float(s)
                return int(s)
            except:
                return 0
        elif input_type == "TEXT":
            return input(label)
        


    # Lists

    def execute_lists_create_with(self, statement):
        print("execute_lists_create_with")
        pointer = statement["pointer"]
        output = []
        for item in pointer:
            output.append(self.execute_statement(item))
        return output

    def execute_lists_repeat(self, statement):
        print("execute_lists_repeat")
        pointer = statement["pointer"]
        times = self.execute_statement(pointer["times"])
        value = self.execute_statement(pointer["value"])
        output = []
        for i in range(times):
            output.append(value)
        return output

    def execute_lists_length(self, statement):
        print("execute_lists_length")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer)
        return len(lst)

    def execute_lists_isEmpty(self, statement):
        print("execute_lists_isEmpty")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer)
        return len(lst) == 0

    def execute_lists_indexOf(self, statement):
        print("execute_lists_indexOf")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["list"])
        item = self.execute_statement(pointer["item"])
        indexOf = pointer["indexOf"]

        if indexOf == "FIRST":
            return lst.index(item) + 1
        elif indexOf == "LAST":
            return lst.index(item) + 1
        return -1

    def execute_lists_getIndex(self, statement):
        print("execute_lists_getIndex")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["list"])
        index = self.execute_statement(pointer["index"])
        mode = pointer["mode"]
        where = pointer["where"]

        if where == "FROM_START":
            if mode == "GET":
                return lst[index - 1]
            elif mode == "REMOVE":
                lst.pop(index - 1)
                return None
            elif mode == "GET_REMOVE":
                return lst.pop(index - 1)
        elif where == "FROM_END":
            if mode == "GET":
                return lst[len(lst) - index]
            elif mode == "REMOVE":
                lst.pop(len(lst) - index)
                return None
            elif mode == "GET_REMOVE":
                return lst.pop(len(lst) - index)
        elif where == "FIRST":
            if mode == "GET":
                return lst[0]
            elif mode == "REMOVE":
                lst.pop(0)
                return None
            elif mode == "GET_REMOVE":
                return lst.pop(0)
        elif where == "LAST":
            if mode == "GET":
                return lst[len(lst) - 1]
            elif mode == "REMOVE":
                lst.pop(len(lst) - 1)
                return None
            elif mode == "GET_REMOVE":
                return lst.pop(len(lst) - 1)
        elif where == "RANDOM":
            if mode == "GET":
                return lst[random.randint(0, len(lst) - 1)]
            elif mode == "REMOVE":
                lst.pop(random.randint(0, len(lst) - 1))
                return None
            elif mode == "GET_REMOVE":
                return lst.pop(random.randint(0, len(lst) - 1))
        return None

    def execute_lists_setIndex(self, statement):
        print("execute_lists_setIndex")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["list"])
        index = self.execute_statement(pointer["index"])
        value = self.execute_statement(pointer["value"])
        where = pointer["where"]
        mode = pointer["mode"]

        if where == "FROM_START":
            if mode == "SET":
                lst[index - 1] = value
            elif mode == "INSERT":
                lst.insert(index - 1, value)
        elif where == "FROM_END":
            if mode == "SET":
                lst[len(lst) - index] = value
            elif mode == "INSERT":
                lst.insert(len(lst) - index, value)
        elif where == "FIRST":
            if mode == "SET":
                lst[0] = value
            elif mode == "INSERT":
                lst.insert(0, value)
        elif where == "LAST":
            if mode == "SET":
                lst[len(lst) - 1] = value
            elif mode == "INSERT":
                lst.insert(len(lst) - 1, value)
        elif where == "RANDOM":
            if mode == "SET":
                lst[random.randint(0, len(lst) - 1)] = value
            elif mode == "INSERT":
                lst.insert(random.randint(0, len(lst) - 1), value)

    def execute_lists_getSublist(self, statement):
        print("execute_lists_getSublist")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["list"])
        where1 = pointer["where1"]
        where2 = pointer["where2"]
        at1 = self.execute_statement(pointer["at1"])
        at2 = self.execute_statement(pointer["at2"])

        if where1 == "FROM_START":
            if where2 == "FROM_START":
                return lst[at1 - 1:at2]
            elif where2 == "FROM_END":
                return lst[at1 - 1:len(lst) - at2]
            elif where2 == "LAST":
                return lst[at1 - 1:len(lst)]
        elif where1 == "FROM_END":
            if where2 == "FROM_START":
                return lst[len(lst) - at2:at1]
            elif where2 == "FROM_END":
                return lst[len(lst) - at2:len(lst) - at1]
            elif where2 == "LAST":
                return lst[len(lst) - at2:len(lst)]
        elif where1 == "FIRST":
            if where2 == "FROM_START":
                return lst[at2 - 1:at1]
            elif where2 == "FROM_END":
                return lst[at2 - 1:len(lst) - at1]
            elif where2 == "LAST":
                return lst[at2 - 1:len(lst)]
        return None

    def execute_lists_split(self, statement):
        print("execute_lists_split")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["value"])
        delim = self.execute_statement(pointer["delim"])
        mode = pointer["mode"]

        if mode == "SPLIT":
            if delim == "":
                return list(lst)
            return lst.split(delim)
        elif mode == "JOIN":
            return delim.join(lst)

    def execute_lists_sort(self, statement):
        print("execute_lists_sort")
        pointer = statement["pointer"]
        lst = self.execute_statement(pointer["list"])
        is_reversed = pointer["direction"] == "-1"
        sort_type = pointer["type"]

        if sort_type == "NUMERIC":
            return sorted(lst, key=lambda x: float(x), reverse=is_reversed)
        elif sort_type == "TEXT":
            return sorted(lst, reverse=is_reversed)
        elif sort_type == "IGNORE_CASE":
            return sorted(lst, key=str.lower, reverse=is_reversed)

    
    # Variables

    def execute_variables_set(self, statement):
        print("execute_variables_set")
        pointer = statement["pointer"]
        variable = pointer["variable"]
        value = self.execute_statement(pointer["value"])

        is_set = False
        if self.current_function_name is not None:
            f = self.find_function(self.current_function_name)
            if variable in f["stack"]:
                f["stack"][variable] = value
                is_set = True
        if not is_set:
            self.variable_stack[variable] = value

    def execute_variables_get(self, statement):
        print("execute_variables_get")
        pointer = statement["pointer"]
        
        if self.current_function_name is not None:
            f = self.find_function(self.current_function_name)
            if pointer in f["stack"]:
                return f["stack"][pointer]
        return self.variable_stack[pointer]

    def execute_math_change(self, statement):
        print("execute_math_change")
        pointer = statement["pointer"]
        variable = pointer["variable"]
        value = self.execute_statement(pointer["value"])

        is_set = False
        if self.current_function_name is not None:
            f = self.find_function(self.current_function_name)
            if variable in f["stack"]:
                f["stack"][variable] = value

                if variable in f["stack"] and (type(f["stack"][variable]) == int or type(f["stack"][variable]) == float):
                    f["stack"][variable] += value
                else:
                    f["stack"][variable] = value

                is_set = True

        if not is_set:
            if variable in self.variable_stack and (type(self.variable_stack[variable]) == int or type(self.variable_stack[variable]) == float):
                self.variable_stack[variable] += value
            else:
                self.variable_stack[variable] = value



    # Functions

    def execute_procedures_defnoreturn(self, statement):
        print("execute_procedures_defnoreturn")
        pointer = statement["pointer"]
        name = pointer["name"]
        args = pointer["args"]
        function_block = pointer["functionBlock"]
        stack = {}
        for arg in args:
            stack[arg] = None
        Engine.function_stack.append({"name": name, "stack": stack, "functionBlock": function_block, "ret": None})

    def execute_procedures_callnoreturn(self, statement):
        print("execute_procedures_callnoreturn")
        pointer = statement["pointer"]
        name = pointer["name"]
        args = pointer["args"]
        self.current_function_name = name
        f = self.find_function(name)
        if f is not None:
            i = 0
            for k, v in f["stack"].items():
                f["stack"][k] = self.execute_statement(args[i])
                i += 1
            self.execute(json.loads(f["functionBlock"]))

        to_ret = None
        if f["ret"] is not None:
            to_ret = self.execute_statement(f["ret"])

        if self.function_return_value is not None:
            to_ret = self.function_return_value

        self.current_function_name = None
        self.function_return = None

        return to_ret
    
    def execute_procedures_ifreturn(self, statement):
        print("execute_procedures_ifreturn")
        pointer = statement["pointer"]
        condition = self.execute_statement(pointer["condition"])
        value = self.execute_statement(pointer["value"])
        if condition:
            self.function_return_value = value
            self.function_return = True

    def execute_procedures_defreturn(self, statement):
        print("execute_procedures_defreturn")
        pointer = statement["pointer"]
        name = pointer["name"]
        args = pointer["args"]
        function_block = pointer["functionBlock"]
        ret = pointer["ret"]
        stack = {}
        for arg in args:
            stack[arg] = None
        Engine.function_stack.append({"name": name, "stack": stack, "functionBlock": function_block, "ret": ret})

    def execute_procedures_callreturn(self, statement):
        return self.execute_procedures_callnoreturn(statement)
