<?php


namespace AppBundle\Entity;

class Account
{
    private $id;

    private $firstname;

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
    public function getFirstname()
    {
        return $this->firstname;
    }

    /**
     * @param mixed $firstname
     */
    public function setFirstname($firstname)
    {
        $this->firstname = $firstname;
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
            "nom" => $this->getFirstname(),
            "prenom" => $this->getLastName(),
            "risque" => $this->getRisk(),
            "solde" => (string) $this->getAmount()
        );

        return $account;
    }
}