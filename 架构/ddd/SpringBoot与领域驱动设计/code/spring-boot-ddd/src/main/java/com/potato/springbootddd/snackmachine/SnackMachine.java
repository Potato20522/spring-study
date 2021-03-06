package com.potato.springbootddd.snackmachine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class SnackMachine {
    private Money moneyInside;
    private Money moneyInTransaction;

    public void insertMoney(Money money) {
        moneyInTransaction = Money.add(moneyInTransaction, money);

    }

    public void returnMoney() {
        //moneyInTransaction = 0
    }

    public void buySnack() {
        moneyInside = Money.add(moneyInside, moneyInTransaction);
        //moneyInTransaction = 0
    }

}
