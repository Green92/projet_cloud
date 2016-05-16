<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use GuzzleHttp;

/**
 * @Route("/account")
 */
class AccountController extends Controller
{
    private $client;

    public function __construct()
    {
        $this->client = new GuzzleHttp\Client([
            'base_uri' => 'http://1.accmanager-1310.appspot.com/account/'
        ]);
    }

    /**
     * @Route("", name="get_account")
     */
    public function getAccountAction(Request $request)
    {
        $response = $this->client->request('GET');

        if ($response->getStatusCode() == 200 && $response->getBody()) {
            $data = json_decode($response->getBody(), true);
        }

        return $this->render('account/account.html.twig', array(
            'data' => $data
        ));
    }

    /**
     * @Route("/{id}/delete", name="delete_account")
     */
    public function deleteAction($id)
    {
        $response = $this->client->request('DELETE', $id);

        return $this->redirectToRoute('get_account');
    }
}
