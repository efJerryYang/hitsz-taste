import re
import os
import sqlparse
from typing import List, Tuple

proper_order = [
    'cafeterias', 'categories', 'merchants', 'contracts',
    'discounts', 'dishes', 'dish_discounts', 'users',
    'merchant_users', 'orders', 'order_items', 'reviews',
    'roles', 'user_roles'
]


def print_tokens(stmt):
    """
    Print the tokens in the given statement. This is for debugging token indexing.
    """
    for i, t in enumerate(stmt.tokens):
        print(f"tokens[{i}]: {t}")


def reorder_tables(sql: str, proper_order: List[str]) -> str:
    """
    Reorder the tables in the given SQL script so that the tables are created in the proper order.
    """
    # Parse the SQL script into a list of statements
    # statements = sqlparse.parse(sql)
    sql = sqlparse.format(sql, strip_comments=True, strip_whitespace=True)
    parsed = sqlparse.parse(sql)
    tables = {}
    prep_drop_alters = []
    post_alters_constraints = []
    other_statements = []
    for stmt in parsed:
        if isinstance(stmt, sqlparse.sql.Statement):
            stmt_type = stmt.get_type()
            # print("statement: ", stmt)
            # print_tokens(stmt)
            if stmt_type == 'CREATE':
                # only support CREATE TABLE
                table_name = stmt.tokens[4].value
                tables.update({table_name: stmt})
                # print("type: CREATE")
            elif stmt_type == 'ALTER':
                if stmt.tokens[2].value.upper() == 'TABLE':
                    table_name = stmt.tokens[4].value
                    if stmt.tokens[6].value.upper() == 'DROP':
                        prep_drop_alters.append(stmt)
                        # print("type: ALTER TABLE DROP")
                    elif stmt.tokens[6].value.upper() == 'ADD':
                        post_alters_constraints.append(stmt)
                        # print("type: ALTER TABLE ADD")
                    else:
                        other_statements.append(stmt)
                        # print("type: ALTER TABLE OTHER")
                else:
                    other_statements.append(stmt)
                    # print("type: ALTER OTHER")
            elif stmt_type == 'DROP':
                # currently include DROP TABLE, and DROP TRIGGER
                # no DROP VIEW and DROP INDEX found in the script
                table_name = stmt.tokens[4].value
                prep_drop_alters.append(stmt)
                # print("type: DROP")
            else:
                other_statements.append(stmt)
                # print("type: OTHER")
        else:
            print(stmt)
            raise Exception("Not a statement")
    # Reorder the tables
    reordered_tables = []
    for table_name in proper_order:
        if table_name in tables:
            reordered_tables.append(tables[table_name])

    # Generate the reordered SQL script
    reordered_stmts = prep_drop_alters + reordered_tables + \
        post_alters_constraints + other_statements
    reordered_sql = '\n'.join([str(stmt) for stmt in reordered_stmts])
    # reindent format not good
    # return sqlparse.format(reordered_sql, keyword_case='upper', identifier_case='lower', reintend=True, reindent_aligned=True, comma_first=True)
    # keyword_case='upper' cause wrong "ROLES" table name, lower cause wrong constraint name
    # return sqlparse.format(reordered_sql, keyword_case='lower', identifier_case='lower', comma_first=True)

    # leave the format as it is
    return reordered_sql

def locate_src_dest_sql() -> Tuple[str, str]:
    """
    Locate the SQL script to be reordered and the destination file.

    +---scripts
    |   +---py
    |   |   create_schema_from_powerdesigner.py
    |   |   encrypt_password.py
    |   |   requirements.txt
    |   |
    |   \---sql
    |       create_database.sql
    ...
    """
    current_dir = os.path.dirname(os.path.abspath(__file__))
    parent_dir = os.path.dirname(current_dir)
    sql_dir = os.path.join(parent_dir, "sql")
    src_sql_file = os.path.join(sql_dir, "create_database.sql")
    dest_sql_file = os.path.join(sql_dir, "create_database_sorted.sql")
    return src_sql_file, dest_sql_file


def main():
    """
    Read the SQL script from the file and reorder the tables.
    """
    src_sql_file, dest_sql_file = locate_src_dest_sql()
    with open(src_sql_file, 'r') as f:
        sql = f.read()

    reordered_sql = reorder_tables(sql, proper_order)
    with open(dest_sql_file, 'w') as f:
        f.write(reordered_sql)

if __name__ == "__main__":
    main()
