defmodule Solution do
  @moduledoc """
  Documentation for `Solution`.
  """

  def solve do
    {:ok, content} = File.read("./input.txt")
    boardingpasses = String.split(content, "\n")
    seat_ids = Enum.map(boardingpasses, &seat_id/1)
    answer1 = seat_ids |> Enum.max
    IO.puts(answer1)
    sorted_seat_ids = seat_ids |> Enum.sort()
    [first | _] = sorted_seat_ids
    [{_,answer}|_] = sorted_seat_ids |> Enum.with_index(first) |> Enum.filter(fn tuple -> elem(tuple, 0) != elem(tuple, 1) end)
    IO.puts(answer)
  end

  def seat_id(line) do
    {row_chars, col_chars} = Enum.split(to_charlist(line), 7)
    row = parse_number(row_chars |> Enum.reverse, <<0::0>>)
    col = parse_number(Enum.map(col_chars, &to_bin/1) |> Enum.reverse, <<0::0>>)
    {row, col}
    row * 8 + col
  end

  def to_bin(char) do
    case char do
      ?R -> ?B
      ?L -> ?F
    end
  end

  def parse_number([head | tail], acc) do
    case head do
      ?F -> parse_number(tail, <<0::1, acc::bitstring>>)
      ?B -> parse_number(tail, <<1::1, acc::bitstring>>)
    end
  end

  def parse_number([], acc) do
    pad_length = 8 - bit_size(acc)
    <<number::8>> = <<0::size(pad_length), acc::bitstring>>
    number
  end
end
