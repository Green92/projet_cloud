<?php


namespace AppBundle\Entity;

class Account
{
    private $id;

    private $firstName;

    private $lastName;

    private $amount;

    private $risk;

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     */
    public function setId($id)
    {
        $this->id = $id;
    }

    /**
     * @return mixed
     */
    public function getFirstName()
    {
        return $this->firstName;
    }

    /**
     * @param mixed $firstname
     */
    public function setFirstName($firstName)
    {
        $this->firstName = $firstName;
    }

    /**
     * @return mixed
     */
    public function getLastName()
    {
        return $this->lastName;
    }

    /**
     * @param mixed $lastName
     */
    public function setLastName($lastName)
    {
        $this->lastName = $lastName;
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
    public function getRisk()
    {
        return $this->risk;
    }

    /**
     * @param mixed $risk
     */
    public function setRisk($risk)
    {
        $this->risk = $risk;
    }

    public function getApiArray()
    {
        $account = array(
            "nom" => $this->getFirstName(),
            "prenom" => $this->getLastName(),
            "risque" => $this->getRisk(),
            "solde" => (string) $this->getAmount()
        );

        return $account;
    }

    public function loadApiJson($json)
    {
        $account = json_decode($json, true);

        $this->setFirstName($account['nom'] ?? null);
        $this->setLastName($account['prenom'] ?? null);
        $this->setRisk($account['risque'] ?? null);
        $this->setAmount($account['solde'] ?? null);
    }
}