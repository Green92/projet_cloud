<?php


namespace AppBundle\Entity;

class Approval
{
    private $accountId;

    private $name;

    private $amount;

    private $response;

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
    public function getName()
    {
        return $this->name;
    }

    /**
     * @param mixed $name
     */
    public function setName($name)
    {
        $this->name = $name;
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

    /**
     * @return mixed
     */
    public function getResponse()
    {
        return $this->response;
    }

    /**
     * @param mixed $response
     */
    public function setResponse($response)
    {
        $this->response = $response;
    }

    public function getApiArray()
    {
        $account = array(
            "accountId" => $this->getAccountId(),
            "nomResponsable" => $this->getName(),
            "amount" => $this->getAmount(),
            "reponseManuelle" => $this->getResponse()
        );

        return $account;
    }

    public function loadApiJson($json)
    {
        $account = json_decode($json, true);

        $this->setAccountId($account['accountId'] ?? null);
        $this->setName($account['nomResponsable'] ?? null);
        $this->setAmount($account['amount'] ?? null);
        $this->setResponse($account['reponseManuelle'] ?? null);
    }
}