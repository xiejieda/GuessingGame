package com.example.guessinggame.bean;

public class GuessingGameTable {
    private int id;
    private int user_1;
    private int user_2;
    private int user_3;
    private int user_4;
    private int last_check;
    private int game_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_1() {
        return user_1;
    }

    public void setUser_1(int user_1) {
        this.user_1 = user_1;
    }

    public int getUser_2() {
        return user_2;
    }

    public void setUser_2(int user_2) {
        this.user_2 = user_2;
    }

    public int getUser_3() {
        return user_3;
    }

    public void setUser_3(int user_3) {
        this.user_3 = user_3;
    }

    public int getUser_4() {
        return user_4;
    }

    public void setUser_4(int user_4) {
        this.user_4 = user_4;
    }

    public int getLast_check() {
        return last_check;
    }

    public void setLast_check(int last_check) {
        this.last_check = last_check;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }
}
