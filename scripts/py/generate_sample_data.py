import os
import random
import faker
import sqlparse

from faker import Factory
from typing import List, Tuple

# Because the salting is done by xor'ing the data with a random number
# the salt can be set to 0 to indicate that the data is not salted.

# salt length in bytes is 16
# zero salt is '00000000000000000000000000000000'


insert_order = [
    'users', 'roles', 'user_roles', 'cafeterias',
    'merchants', 'contracts', 'discounts', 'categories',
    'dishes', 'dish_discounts', 'orders', 'order_items',
    'reviews', 'merchant_users'
]

user_id = 1000
category_id = 0
cafeteria_id = 0
merchant_id = 0
contract_id = 0
discount_id = 0
dish_id = 0
order_id = 0
review_id = 0
role_id = 0

user_id_list = []
category_id_list = []
cafeteria_id_list = []
merchant_id_list = []
contract_id_list = []
discount_id_list = []
dish_id_list = []
order_id_list = []
review_id_list = []
role_id_list = []


def next_user_id():
    global user_id
    user_id += 1
    user_id_list.append(user_id)
    return user_id


def select_user_id():
    r = random.randint(0, len(user_id_list) - 1)
    return user_id_list[r]


def next_category_id():
    global category_id
    category_id += 1
    category_id_list.append(category_id)
    return category_id


def select_category_id():
    r = random.randint(0, len(category_id_list) - 1)
    return category_id_list[r]


def next_cafeteria_id():
    global cafeteria_id
    cafeteria_id += 1
    cafeteria_id_list.append(cafeteria_id)
    return cafeteria_id


def select_cafeteria_id():
    r = random.randint(0, len(cafeteria_id_list) - 1)
    return cafeteria_id_list[r]


def next_merchant_id():
    global merchant_id
    merchant_id += 1
    merchant_id_list.append(merchant_id)
    return merchant_id


def select_merchant_id():
    r = random.randint(0, len(merchant_id_list) - 1)
    return merchant_id_list[r]


def next_contract_id():
    global contract_id
    contract_id += 1
    contract_id_list.append(contract_id)
    return contract_id


def select_contract_id():
    r = random.randint(0, len(contract_id_list) - 1)
    return contract_id_list[r]


def next_discount_id():
    global discount_id
    discount_id += 1
    discount_id_list.append(dish_id)
    return discount_id


def select_discount_id():
    r = random.randint(0, len(discount_id_list) - 1)
    return discount_id_list[r]


def next_dish_id():
    global dish_id
    dish_id += 1
    dish_id_list.append(dish_id)
    return dish_id


def select_dish_id():
    r = random.randint(0, len(dish_id_list) - 1)
    return dish_id_list[r]


def next_order_id():
    global order_id
    order_id += 1
    order_id_list.append(order_id)
    return order_id


def select_order_id():
    r = random.randint(0, len(order_id_list) - 1)
    return order_id_list[r]


def next_review_id():
    global review_id
    review_id += 1
    review_id_list.append(review_id)
    return review_id


def select_review_id():
    r = random.randint(0, len(review_id_list) - 1)
    return review_id_list[r]


def next_role_id():
    global role_id
    role_id += 1
    role_id_list.append(role_id)
    return role_id


def select_role_id():
    r = random.randint(0, len(role_id_list) - 1)
    return role_id_list[r]


def select_job_title():
    """
    manager|contractor|merchant
    """

    r = random.random()
    if r < 0.2:
        return 'manager'
    elif r < 0.4:
        return 'contractor'
    else:
        return 'merchant'


def select_company():
    """
    company1|company2|company3
    """

    r = random.random()
    if r < 0.3:
        return 'company1'
    elif r < 0.6:
        return 'company2'
    else:
        return 'company3'


admin_cnt = 0
staff_cnt = 0
default_cnt = 0


def select_role():
    """
    admin|staff|customer|default
    """
    global admin_cnt
    global staff_cnt
    global default_cnt

    r = random.random()
    if admin_cnt == 0:
        admin_cnt += 1
        return 'admin'
    elif staff_cnt == 0:
        staff_cnt += 1
        return 'staff'
    elif default_cnt == 0:
        default_cnt += 1
        return 'default'

    if r < 0.01:
        return 'admin'
    elif r < 0.1:
        return 'staff'
    elif r < 0.15:
        return 'default'
    else:
        return 'customer'


def parse_column_names(create_stmt: str) -> List[str]:
    # Split the CREATE TABLE statement into two parts: the column definitions and the primary key definition
    column_defs, _, primary_key_def = create_stmt.partition("primary key")
    # Split the column definitions into a list of individual column definitions
    column_defs = column_defs.strip('(').strip(')').split(",")
    # Extract the column names from the column definitions
    column_names = []
    for col in column_defs:
        col = col.strip().split(' ')[0]
        if col != '':
            column_names.append(col)
    return column_names


def generate_sample_data(create_statements: List[str], insert_order: List[str]) -> str:
    fake = Factory.create(locale='zh_CN')
    sql_script = ""
    for table_name in insert_order:
        print("Generating sample data for table:", table_name, end='')
        create_stmt = [
            stmt for stmt in create_statements if table_name in stmt][0]
        # Extract the column names from the CREATE TABLE statement
        stmt = sqlparse.parse(create_stmt)[0].tokens[6]  # ( ... )
        column_names = parse_column_names(str(stmt))
        n = 20
        print(' n={}'.format(n))
        for _ in range(n):
            # Generate the INSERT statement
            insert_stmt = "INSERT INTO {} ({}) VALUES (".format(
                table_name, ", ".join(column_names))
            # print("column_names: {}".format(column_names))
            for col in column_names:
                if col == 'user_id':
                    if table_name == 'users':
                        insert_stmt += "{}, ".format(next_user_id())
                    else:
                        insert_stmt += "{}, ".format(select_user_id())
                elif col == 'salt':
                    insert_stmt += "'00000000000000000000000000000000', "
                elif col == 'password':
                    insert_stmt += "'{}', ".format(fake.password())
                elif col == 'email':
                    insert_stmt += "'{}', ".format(fake.email())
                elif col == 'phone':
                    insert_stmt += "'{}', ".format(fake.phone_number())
                elif col == 'address' or col == 'location':
                    insert_stmt += "'{}', ".format(
                        ', '.join(fake.address().split('\n')))
                elif col == 'username':
                    insert_stmt += "'{}', ".format(fake.user_name())
                elif col == 'create_at':
                    insert_stmt += "now(), "
                elif col == 'cafeteria_id':
                    if table_name == 'cafeterias':
                        insert_stmt += "{}, ".format(next_cafeteria_id())
                    else:
                        insert_stmt += "{}, ".format(select_cafeteria_id())
                elif col == 'name':
                    if table_name == 'cafeterias':
                        insert_stmt += "'Cafeteria {}', ".format(category_id)
                    elif table_name == 'categories':
                        insert_stmt += "'Category {}', ".format(category_id)
                    elif table_name == 'merchants':
                        insert_stmt += "'Merchant {}', ".format(merchant_id)
                    elif table_name == 'dishes':
                        insert_stmt += "'{}', ".format(fake.sentences(nb=1)[0])
                    elif table_name == 'discounts':
                        insert_stmt += "'{}', ".format(fake.sentences(nb=1)[0])
                    elif table_name == 'roles':
                        insert_stmt += "'{}', ".format(select_role())
                    else:
                        raise Exception(
                            "No match table_name {} exception!".format(table_name))
                elif col == 'is_active':
                    insert_stmt += "true, "
                elif col == 'category_id':
                    if table_name == 'categories':
                        insert_stmt += "{}, ".format(next_category_id())
                    else:
                        insert_stmt += "{}, ".format(select_category_id())
                elif col == 'merchant_id':
                    if table_name == 'merchants':
                        insert_stmt += "{}, ".format(next_merchant_id())
                    else:
                        insert_stmt += "{}, ".format(select_merchant_id())
                elif col == 'start_timestamp':
                    insert_stmt += "now(), "
                elif col == 'end_timestamp':  # next year timestamp
                    if table_name == 'contracts':
                        insert_stmt += "now() + interval '1 year', "
                    elif table_name == 'dish_discounts':
                        insert_stmt += "now() + interval '1 month', "
                    else:
                        insert_stmt += "now() + interval '1 day', "
                elif col == 'discount_id':
                    if table_name == 'discounts':
                        insert_stmt += "{}, ".format(next_discount_id())
                    else:
                        insert_stmt += "{}, ".format(select_discount_id())
                elif col == 'percentage':  # float (0, 1) like 0.12 or 0.93
                    insert_stmt += "'{:0.2f}', ".format(random.random())
                elif col == 'price':  # random float
                    insert_stmt += "'{:.2f}', ".format(random.random() * 100)
                elif col == 'dish_id':
                    if table_name == 'dishes':
                        insert_stmt += "{}, ".format(next_dish_id())
                    else:
                        insert_stmt += "{}, ".format(select_dish_id())
                elif col == 'ingredients':
                    insert_stmt += "'{}', ".format(fake.sentences(nb=1)[0])
                elif col == 'description':  # more than 3 sentences
                    insert_stmt += "'{}', ".format(fake.sentences(nb=3)[0])
                elif col == 'firstname':
                    insert_stmt += "'{}', ".format(fake.first_name())
                elif col == 'lastname':
                    insert_stmt += "'{}', ".format(fake.last_name())
                elif col == 'id_number':  # fill NULL
                    insert_stmt += "NULL, "
                elif col == 'job_title':
                    # insert_stmt += "'{}', ".format(fake.job())
                    insert_stmt += "'{}', ".format(select_job_title())
                elif col == 'company':
                    insert_stmt += "'{}', ".format(select_company())
                elif col == 'contact':
                    insert_stmt += "'{}', ".format(fake.phone_number())
                elif col == 'total_price':
                    insert_stmt += "'{:.2f}', ".format(random.random() * 100)
                elif col == 'status':
                    if table_name == 'orders':
                        insert_stmt += "'{}', ".format("completed")
                    else:
                        insert_stmt += "'{}', ".format("unknownstatus")
                elif col == 'quantity':
                    insert_stmt += "'{}', ".format(random.randint(1, 10))
                elif col == 'rating':
                    insert_stmt += "'{}', ".format(random.randint(1, 5))
                elif col == 'comment':
                    insert_stmt += "'{}', ".format(fake.sentences(nb=5)[0])
                elif col == 'grant_date':
                    insert_stmt += "now(), "
                elif col == 'role_id':
                    if table_name == 'roles':
                        insert_stmt += "{}, ".format(next_role_id())
                    else:
                        insert_stmt += "{}, ".format(select_role_id())
                elif col == 'order_id':
                    if table_name == 'orders':
                        insert_stmt += "{}, ".format(next_order_id())
                    else:
                        insert_stmt += "{}, ".format(select_order_id())
                elif col == 'review_id':
                    if table_name == 'orders':  # it is important to notice that, we insert review_id before create review data
                        insert_stmt += "{}, ".format(next_review_id())
                    else:
                        insert_stmt += "{}, ".format(select_review_id())
                elif col == 'update_time':
                    insert_stmt += "now(), "
                else:
                    raise Exception("Unknown column: {}".format(col))
            sql_script += insert_stmt.strip()[:-1] + ");\n"
    return sql_script


def locate_src_dest_sql() -> Tuple[str, str]:
    """
    Locate the SQL script to be reordered and the destination file.

    +---scripts
    |   +---py
    |   |   create_schema_from_powerdesigner.py
    |   |   encrypt_password.py
    |   |   generate_sample_data.py
    |   |   requirements.txt
    |   |
    |   \---sql
    |       create_database_sorted.sql
    ...
    """
    current_dir = os.path.dirname(os.path.abspath(__file__))
    parent_dir = os.path.dirname(current_dir)
    sql_dir = os.path.join(parent_dir, "sql")
    src_sql_file = os.path.join(sql_dir, "create_database_sorted.sql")
    dest_sql_file = os.path.join(sql_dir, "generate_sample_data.sql")
    return src_sql_file, dest_sql_file


def main():
    src_file, dest_file = locate_src_dest_sql()
    with open(src_file, 'r') as f:
        sql_src = f.read()

    sql = sqlparse.format(sql_src, strip_comments=True, strip_whitespace=True)
    sql_dest = generate_sample_data([str(stmt) for stmt in sqlparse.parse(
        sql) if stmt.get_type() == 'CREATE'], insert_order)

    with open(dest_file, 'w') as f:
        f.write(sql_dest)


if __name__ == '__main__':
    main()
