#!/bin/sh

die () {
    echo >&2 "$@"
    exit 1
}

# Find the next day number by checking existing directories
DAY_NUM=1
while [ "$DAY_NUM" -le 12 ]; do
    DAY_CHECK=$(printf "day%02d" "$DAY_NUM")
    if [ ! -d "src/$DAY_CHECK" ]; then
        break
    fi
    DAY_NUM=$((DAY_NUM + 1))
done

if [ "$DAY_NUM" -gt 12 ]; then
    die "Error: All days (1-12) already exist"
fi

echo "Creating day $DAY_NUM..."

# Format day number with leading zero if needed
DAY_FORMATTED=$(printf "day%02d" "$DAY_NUM")
DAY_DIR="src/$DAY_FORMATTED"

# Validate that the directory doesn't exist
if [ -d "$DAY_DIR" ]; then
    die "Error: Directory $DAY_DIR already exists"
fi

# Create directory structure
echo "Creating $DAY_DIR..."
mkdir -p "$DAY_DIR/data"

# Create empty text files
touch "$DAY_DIR/data/input.txt"
touch "$DAY_DIR/data/test-input.txt"

# Create main.kt with template code
cat > "$DAY_DIR/main.kt" << 'EOF'
@file:Suppress("ktlint:standard:filename")

package dayXX

import utils.readFromPath
import utils.shouldBe
import kotlin.time.measureTimedValue

fun main() {
    val basePath = "dayXX/data"

    // TEST for part 1
    part1(readFromPath("$basePath/test-input.txt")) shouldBe 1

    // TEST for part 2
    part2(readFromPath("$basePath/test-input.txt")) shouldBe 1

    println("Solution of dayXX:")
    val input = readFromPath("$basePath/input.txt")
    measureTimedValue { part1(input) }.let { (part1, time) ->
        println("Part 1: $part1 took $time")
    }
    measureTimedValue { part2(input) }.let { (part2, time) ->
        println("Part 2: $part2 took $time")
    }
}

fun part1(input: Sequence<String>): Int = input.toList().size

fun part2(input: Sequence<String>): Int = input.toList().size
EOF

# Replace XX with the formatted day number
sed -i '' "s/dayXX/$DAY_FORMATTED/g" "$DAY_DIR/main.kt"

echo "Successfully created $DAY_DIR with template files"
