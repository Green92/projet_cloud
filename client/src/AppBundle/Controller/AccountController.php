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
    /**
     * @Route("", name="get_account")
     */
    public function accountAction(Request $request)
    {
        $client = new GuzzleHttp\Client([
            'base_uri' => 'http://1.accmanager-1310.appspot.com'
        ]);

        $response = $client->request('GET', 'account');

        if ($response->getStatusCode() == 200 && $response->getBody()) {
            $data = json_decode($response->getBody(), true);
        }

        return $this->render('account/account.html.twig', array(
            'data' => $data
        ));
    }
}
