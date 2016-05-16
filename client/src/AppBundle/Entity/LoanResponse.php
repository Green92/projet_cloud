<?php


namespace AppBundle\Entity;

class LoanResponse
{
    private $response;

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

    public function loadApiJson($json)
    {
        $response = json_decode($json, true);

        $this->setResponse($response['reponseManuelle'] ?? null);
    }
}