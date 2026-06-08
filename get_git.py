import subprocess
result = subprocess.run(['git', 'log', '-p', '-1', 'ad27827ee02824d2722343f40d9bdcc7dccba8c5'], capture_output=True, text=True)
print(result.stdout)
