<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use GuzzleHttp;

class DefaultController extends Controller
{
    /**
     * @Route("/", name="homepage")
     */
    public function indexAction(Request $request)
    {
        // replace this example code with whatever you need
        return $this->render('default/index.html.twig', [
            'base_dir' => realpath($this->getParameter('kernel.root_dir').'/..'),
        ]);
    }

    /**
     * @Route("/account", name="account")
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

        return $this->render('default/account.html.twig', array(
            'data' => $data
        ));
    }
}
