<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use GuzzleHttp;

/**
 * @Route("/approval")
 */
class ApprovalController extends Controller
{
    /**
     * @Route("", name="get_approval")
     */
    public function approvalAction(Request $request)
    {
        $client = new GuzzleHttp\Client([
            'base_uri' => 'http://1.appmanager-1311.appspot.com/'
        ]);

        $response = $client->request('GET', 'approval');

        if ($response->getStatusCode() == 200 && $response->getBody()) {
            $data = json_decode($response->getBody(), true);
        }

        return $this->render('approval/approval.html.twig', array(
            'data' => $data
        ));
    }
}
