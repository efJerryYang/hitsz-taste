import re
import sqlparse
from typing import List

proper_order = [
    'cafeterias', 'categories', 'merchants', 'contracts',
    'discounts', 'dishes', 'dish_discounts', 'users',
    'merchant_users', 'orders', 'order_items', 'reviews',
    'roles', 'user_roles'
]

def reorder_tables(sql: str, proper_order: List[str]) -> str:
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
            print("statement: ", stmt)
            for i, t in enumerate(stmt.tokens):
                print(f"tokens[{i}]: {t}")
            if stmt_type == 'CREATE':
                # only support CREATE TABLE
                table_name = stmt.tokens[4].value
                tables.update({table_name: stmt})
                print("type: CREATE")
            elif stmt_type == 'ALTER':
                if stmt.tokens[2].value.upper() == 'TABLE':
                    table_name = stmt.tokens[4].value
                    if stmt.tokens[6].value.upper() == 'DROP':
                        prep_drop_alters.append(stmt)
                        print("type: ALTER TABLE DROP")
                    elif stmt.tokens[6].value.upper() == 'ADD':
                        post_alters_constraints.append(stmt)
                        print("type: ALTER TABLE ADD")
                    else:
                        other_statements.append(stmt)
                        print("type: ALTER TABLE OTHER")
                else:
                    other_statements.append(stmt)
                    print("type: ALTER OTHER")
            elif stmt_type == 'DROP':
                # currently include DROP TABLE, and DROP TRIGGER
                # no DROP VIEW and DROP INDEX found in the script
                table_name = stmt.tokens[4].value
                prep_drop_alters.append(stmt)
                print("type: DROP")
            else:
                other_statements.append(stmt)
                print("type: OTHER")
        else:
            print(stmt)
            raise Exception("Not a statement")
    # Reorder the tables
    reordered_tables = []
    for table_name in proper_order:
        if table_name in tables:
            reordered_tables.append(tables[table_name])

    # Generate the reordered SQL script
    reordered_stmts = prep_drop_alters + reordered_tables + post_alters_constraints + other_statements
    reordered_sql = '\n'.join([str(stmt) for stmt in reordered_stmts])
    # reindent format not good
    # return sqlparse.format(reordered_sql, keyword_case='upper', identifier_case='lower', reintend=True, reindent_aligned=True, comma_first=True)
    # keyword_case='upper' cause wrong "ROLES" table name
    return sqlparse.format(reordered_sql, keyword_case='lower', identifier_case='lower', comma_first=True)

def main():
    # Read the SQL script from the file
    with open("create_tables.sql", "r") as f:
        sql = f.read()

    # Reorder the tables and write the reordered script to the file
    reordered_sql = reorder_tables(sql, proper_order)
    with open("create_tables_sorted.sql", "w") as f:
        f.write(reordered_sql)

if __name__ == "__main__":
    main()
