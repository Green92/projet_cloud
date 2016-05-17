<?php


namespace AppBundle\Entity;

class LoanRequest
{
    private $accountId;

    private $amount;

    /**
     * @return mixed
     */
    public function getAccountId()
    {
        return $this->accountId;
    }

    /**
     * @param mixed $accountId
     */
    public function setAccountId($accountId)
    {
        $this->accountId = $accountId;
    }

    /**
     * @return mixed
     */
    public function getAmount()
    {
        return $this->amount;
    }

    /**
     * @param mixed $amount
     */
    public function setAmount($amount)
    {
        $this->amount = $amount;
    }

    public function getApiArray()
    {
        $request = array(
            "accountId" => $this->getAccountId(),
            "amount" => $this->getAmount()
        );

        return $request;
    }

    public function getApiWithoutAmountArray()
    {
        $request = array(
            "accountId" => $this->getAccountId()
        );

        return $request;
    }
}