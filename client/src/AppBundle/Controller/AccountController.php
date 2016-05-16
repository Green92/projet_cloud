<?php

namespace AppBundle\Controller;

use AppBundle\Entity\Account;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
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

    private function getAddForm($account)
    {
        return $this->createFormBuilder($account)
            ->add('firstName', TextType::class, array('required' => true))
            ->add('lastName', TextType::class, array('required' => true))
            ->add('risk', ChoiceType::class, array(
                'choices'  => array(
                    'high' => "high",
                    'low' => "low"
                )
            ))
            ->add('amount', NumberType::class, array('required' => true))
            ->getForm();
    }

    /**
     * @Route("", name="get_account")
     */
    public function getAction(Request $request)
    {
        $response = $this->client->request('GET');

        if ($response->getStatusCode() == 200 && $response->getBody()) {
            $data = json_decode($response->getBody(), true);
        }

        $account = new Account();
        $form = $this->getAddForm($account);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {

            $response = $this->client->request('POST', null, [
                'json' => $account->getApiArray()
            ]);

            return $this->redirectToRoute('get_account');
        }

        return $this->render('account/account.html.twig', array(
            'form' => $form->createView(),
            'data' => $data
        ));
    }

    /**
     * @Route("/{id}/edit", name="edit_account")
     */
    public function editAction($id, Request $request)
    {
        $response = $this->client->request('GET', $id);

        $account = new Account();
        $account->loadApiJson($response->getBody());

        $form = $this->getAddForm($account);

        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $response = $this->client->request('PUT', $id, [
                'json' => $account->getApiArray()
            ]);

            return $this->redirectToRoute('get_account');
        }

        return $this->render('account/edit.html.twig', array(
            'form' => $form->createView()
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
