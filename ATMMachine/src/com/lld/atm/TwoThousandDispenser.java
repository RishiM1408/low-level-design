package com.lld.atm;

public class TwoThousandDispenser extends CashDispenser {
    @Override
    protected int getDenomination() {
        return 2000;
    }
}

class FiveHundredDispenser extends CashDispenser {
    @Override
    protected int getDenomination() {
        return 500;
    }
}

class OneHundredDispenser extends CashDispenser {
    @Override
    protected int getDenomination() {
        return 100;
    }
}
